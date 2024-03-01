package com.invoiceservice.invoiceservice.web.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceControllerTest {

    @Test
    void findAllClients() {
    }



//    @Test
//    void TrainingController_CreateNewTrainingDateOptional() throws Exception {
//
//        Training trainingMock = Training.builder()
//                .name("Monkey")
//                .startDate("2023-03-15")
//                .trainingTypeId(1L)
//                .endDate(null).build();
//
//        when(serviceMock.createNewTraining(trainingMock)).thenReturn(trainingMock);
//
//        mockMvc.perform(post(URL + "/new-training")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(trainingMock)))
//                .andExpect(jsonPath("$.name").value("Monkey"))
//                .andExpect(jsonPath("$.startDate").value("2023-03-15"))
//                .andExpect(jsonPath("$.trainingTypeId").value(1L))
//                .andExpect(jsonPath("$.endDate").value(trainingMock.getEndDate()))
//                .andExpect(status().isCreated())
//                .andDo(print());
//
//        ResponseEntity<Training> newTrainingCreated = new ResponseEntity<>(HttpStatus.CREATED);
//
//        Assertions.assertEquals(HttpStatus.CREATED, newTrainingCreated.getStatusCode());
//        verify(serviceMock, times(1)).createNewTraining(trainingMock);
//
//    }
//
//    @Test
//    void TrainingController_CreateNewTrainingDateOptional_BadRequest() throws Exception {
//
//        Training someTrainingDetails = new Training();
//        BindingResult bindingResult = mock(BindingResult.class);
//        when(bindingResult.hasErrors()).thenReturn(true);
//
//        ResponseEntity<Training> noResult = trainingControllerTarget
//                .createNewTraining(someTrainingDetails, bindingResult);
//
//        Assertions.assertEquals(HttpStatus.BAD_REQUEST, noResult.getStatusCode());
//        Assertions.assertNull(noResult.getBody());
//        verifyNoInteractions(serviceMock);
//    }
}