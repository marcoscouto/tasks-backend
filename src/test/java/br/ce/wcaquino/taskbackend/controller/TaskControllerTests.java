package br.ce.wcaquino.taskbackend.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.hibernate.action.spi.Executable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import br.ce.wcaquino.taskbackend.model.Task;
import br.ce.wcaquino.taskbackend.repo.TaskRepo;
import br.ce.wcaquino.taskbackend.utils.ValidationException;

public class TaskControllerTests {

    @Mock
    TaskRepo todoRepo;

    @InjectMocks
    TaskController controller;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void naoDeveSalvarTarefasSemDescricao(){
        Task todo = new Task();
        try {
            controller.save(todo);
        } catch (Exception e) {
            assertEquals(ValidationException.class, e.getClass());
        }
    }

    @Test
    public void naoDeveSalvarTarefasSemData(){
        Task todo = new Task();
        todo.setTask("task");
        try {
            controller.save(todo);
        } catch (Exception e) {
            assertEquals(ValidationException.class, e.getClass());
        }
    }

    @Test
    public void naoDeveSalvarTarefasComDataPassada() throws ValidationException {
        Task todo = new Task();
        todo.setTask("task");
        todo.setDueDate(LocalDate.now().minusDays(1));
        try {
            controller.save(todo);
        } catch (Throwable e) {
            assertEquals(ValidationException.class, e.getClass());
        }
    }

    @Test
    public void deveSalvarTarefaComSucesso() throws ValidationException{
        Task todo = new Task();
        todo.setTask("task");
        todo.setDueDate(LocalDate.now().plusDays(1));
        todo.setId(1L);
        Mockito.when(todoRepo.save(todo)).thenReturn(todo);

        ResponseEntity<Task> result = controller.save(todo);
        assertTrue(result.getStatusCode().is2xxSuccessful());
    }
    
}
