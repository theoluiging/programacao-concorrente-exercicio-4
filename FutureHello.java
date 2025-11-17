/* Disciplina: Programacao Concorrente */
/* Prof.: Silvana Rossetto */
/* Laboratório: 11 */
/* Codigo: Exemplo de uso de futures */
/* -------------------------------------------------------------------*/

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import java.util.ArrayList;
import java.util.List;

class VerificaPrimoCallable implements Callable<Integer> {
  private long numero;

  public VerificaPrimoCallable(long numero) {
    this.numero = numero;
  }

  // Função para determinar se um numero é primo
  private boolean ehPrimo(long n) {
    if (n <= 1) return false;
    if (n == 2) return true;
    if (n % 2 == 0) return false;
    for (long i = 3; i < Math.sqrt(n)+1; i += 2) {
      if (n % i == 0) return false;
    }
    return true;
  }

  public Integer call() throws Exception {
      if (ehPrimo(this.numero)) {
        return 1;
      } else {
        return 0;
      }
  }
}

//classe do método main
public class FutureHello  {
  private static final int N = 1000000;
  private static final int NTHREADS = 10;

  public static void main(String[] args) {
    //cria um pool de threads (NTHREADS)
    ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);
    //cria uma lista para armazenar referencias de chamadas assincronas
    List<Future<Integer>> list = new ArrayList<Future<Integer>>();

    for (int i = 0; i < N; i++) {
      Callable<Integer> worker = new VerificaPrimoCallable(i);    

      Future<Integer> submit = executor.submit(worker);
      list.add(submit);
    }

    System.out.println("Tarefas submetidas: " + list.size());
        
    //recupera os resultados e faz o somatório final
    long totalPrimos = 0;
    for (Future<Integer> future : list) {
      try {
        totalPrimos += future.get(); //bloqueia ate a tarefa terminar e retorna o resultado (0 ou 1)
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    }
        
    System.out.println("Total de numeros primos entre 1 e " + N + ": " + totalPrimos);
    executor.shutdown();
  }
}