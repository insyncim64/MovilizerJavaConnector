package com.movilizer.jc;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.movilitas.jaxbframework.MovilizerJaxbException;
import com.movilitas.jaxbframework.dataaccess.jaxb.v14.MovilizerMasterdataAck;
import com.movilitas.jaxbframework.dataaccess.jaxb.v14.MovilizerResponse;
import com.movilitas.jaxbframework.dataaccess.jaxb.v14.MovilizerUploadDataContainer;

public class PollingServer
{
    private MovilizerWS _connection;
    private boolean     _run = true;
    private long        _interval;
    private String      _queue;

    public PollingServer(final MovilizerWS conn, final long interval, final String queue)
    {
        this._connection = conn;
        this._interval = interval;
        this._queue = queue;
    }

    public void startServer()
    {
        String ackKey = null;
        while (this._run)
        {
            if (this._connection.isDebug())
            {
                System.out.println("Polling run started.");
            }

            try
            {
                Thread.sleep(this._interval);
            }
            catch (final InterruptedException e1)
            {
                e1.printStackTrace();
            }

            try
            {
                final MovilizerResponse response = this._connection.fireEmptyRequest(ackKey, this._queue);

                ackKey = response.getRequestAcknowledgeKey();

                final List<MovilizerUploadDataContainer> containerList = response.getUploadContainerList();
                final long currentTime = System.currentTimeMillis();
                PrintWriter writer;
                final int numResp = 0;

                final List<MovilizerMasterdataAck> mdAckList = response.getMasterdataAckList();

                if (containerList != null)
                {
                    for (final MovilizerUploadDataContainer container : containerList)
                    {
                        final File file = new File("./responses/containers/Response" + currentTime + "-" + numResp + ".xml", "UTF-8");
                        if (!file.exists())
                        {
                            try
                            {
                                if (file.createNewFile())
                                {
                                    writer = new PrintWriter(file);
                                    writer.write(this._connection.toXML(container));
                                    writer.close();
                                }
                                if (this._connection.isDebug())
                                {
                                    System.out.println("Wrote container to file " + file.getPath());
                                }
                            }
                            catch (final IOException e)
                            {
                                e.printStackTrace();
                                ackKey = null;
                            }
                            catch (final MovilizerJaxbException e)
                            {
                                e.printStackTrace();
                                ackKey = null;
                            }
                        }
                    }
                }

                if (mdAckList != null)
                {
                    final File file = new File("./responses/containers/Response" + currentTime + "-" + numResp + ".xml", "UTF-8");
                    if (!file.exists())
                    {
                        if (file.createNewFile())
                        {
                            writer = new PrintWriter(file);
                            for (final MovilizerMasterdataAck ack : mdAckList)
                            {
                                writer.write(ack.getPool() + "-" + ack.getGroup() + "-" + ack.getKey() + "\n");
                            }
                            writer.close();
                        }
                    }
                    if (this._connection.isDebug())
                    {
                        System.out.println("Wrote ack to file " + file.getPath());
                    }
                }

            }
            catch (final Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
