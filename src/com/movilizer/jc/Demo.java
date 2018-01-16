package com.movilizer.jc;

import com.movilitas.jaxbframework.dataaccess.jaxb.v14.MoveletConstants;
import com.movilitas.jaxbframework.dataaccess.jaxb.v14.MoveletType;
import com.movilitas.jaxbframework.dataaccess.jaxb.v14.MovilizerAnswer;
import com.movilitas.jaxbframework.dataaccess.jaxb.v14.MovilizerGenericDataContainer;
import com.movilitas.jaxbframework.dataaccess.jaxb.v14.MovilizerGenericDataContainerEntry;
import com.movilitas.jaxbframework.dataaccess.jaxb.v14.MovilizerMasterdataPoolUpdate;
import com.movilitas.jaxbframework.dataaccess.jaxb.v14.MovilizerMasterdataUpdate;
import com.movilitas.jaxbframework.dataaccess.jaxb.v14.MovilizerMovelet;
import com.movilitas.jaxbframework.dataaccess.jaxb.v14.MovilizerMoveletDelete;
import com.movilitas.jaxbframework.dataaccess.jaxb.v14.MovilizerMoveletSet;
import com.movilitas.jaxbframework.dataaccess.jaxb.v14.MovilizerParticipant;
import com.movilitas.jaxbframework.dataaccess.jaxb.v14.MovilizerQuestion;
import com.movilitas.jaxbframework.dataaccess.jaxb.v14.MovilizerRequest;
import com.movilitas.jaxbframework.dataaccess.jaxb.v14.MovilizerResponse;
import com.movilitas.sync.SyncConstants;

public class Demo
{
    private static String DEVICE_ADDRESS = "";

    public static void main(final String... args) throws Exception
    {
        final MovilizerWS ws = new MovilizerWS();

        createMovelet(ws);
        deleteMovelet(ws);
        createMasterdata(ws);
    }

    private static MovilizerResponse createMovelet(final MovilizerWS ws) throws Exception
    {

        final MovilizerRequest movilizerRequest = new MovilizerRequest();
        movilizerRequest.setSynchronousResponse(true);
        movilizerRequest.setNumResponses(new Integer(100));

        final MovilizerMoveletSet moveletSet = new MovilizerMoveletSet();

        final MoveletType moveletType = MoveletType.SINGLE;

        final MovilizerMovelet movelet = new MovilizerMovelet();
        movelet.setMoveletKey("MyMoveletKey");
        movelet.setMoveletType(moveletType);
        movelet.setName("MyMovelet");
        movelet.setInitialQuestionKey("Q1");

        final MovilizerQuestion question = new MovilizerQuestion();
        movelet.addQuestion(question);
        question.setKey("Q1");
        question.setType(SyncConstants.QTYPE_MESSAGE);
        question.setTitle("MyMessageScreen");

        final MovilizerAnswer answer = new MovilizerAnswer();
        question.addAnswer(answer);
        answer.setKey("Q1_A1");
        answer.setText("MyMessage");
        answer.setNextQuestionKey(MoveletConstants.QNUM_END);

        final MovilizerParticipant participant = new MovilizerParticipant();
        participant.setDeviceAddress(DEVICE_ADDRESS);
        participant.setParticipantKey("");
        participant.setName("test");

        moveletSet.addMovelet(movelet);
        moveletSet.addParticipant(participant);
        movilizerRequest.addMoveletSet(moveletSet);

        return ws.sendRequest(movilizerRequest);
    }

    private static MovilizerResponse deleteMovelet(final MovilizerWS ws) throws Exception
    {
        final MovilizerRequest movilizerRequest = new MovilizerRequest();
        movilizerRequest.setSynchronousResponse(true);
        movilizerRequest.setNumResponses(new Integer(100));

        final MovilizerMoveletDelete moveletDelete = new MovilizerMoveletDelete();
        moveletDelete.setMoveletKey("MyMoveletKey");

        movilizerRequest.addMoveletDelete(moveletDelete);

        return ws.sendRequest(movilizerRequest);
    }

    private static MovilizerResponse createMasterdata(final MovilizerWS ws) throws Exception
    {

        final MovilizerRequest movilizerRequest = new MovilizerRequest();
        movilizerRequest.setSynchronousResponse(true);
        movilizerRequest.setNumResponses(new Integer(100));

        final MovilizerMasterdataPoolUpdate masterdataPoolUpdate = new MovilizerMasterdataPoolUpdate();
        final MovilizerMasterdataUpdate masterdataUpdate = new MovilizerMasterdataUpdate();
        final MovilizerGenericDataContainer data = new MovilizerGenericDataContainer();

        final MovilizerGenericDataContainerEntry outer = new MovilizerGenericDataContainerEntry();
        final MovilizerGenericDataContainerEntry inner = new MovilizerGenericDataContainerEntry();

        masterdataPoolUpdate.setPool("myPool");

        masterdataUpdate.setGroup("myGroup");

        masterdataUpdate.setKey("myKey");
        masterdataUpdate.setFilter1("filter1");
        masterdataUpdate.setDescription("Some Description");

        inner.setName("innerName");
        inner.setValstr("myInnerValue");
        outer.setName("outerName");

        outer.addEntry(inner);
        data.addEntry(outer);
        masterdataUpdate.setData(data);

        masterdataPoolUpdate.addUpdate(masterdataUpdate);

        movilizerRequest.addMasterdataPoolUpdate(masterdataPoolUpdate);

        return ws.sendRequest(movilizerRequest);
    }
}
