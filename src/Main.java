import builders.StudentsBuilder;
import entities.Studant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner;
    private static List<Studant> sortedStudentListByAverage;

    public static void main(String[] args) {
        var allStudents = StudentsBuilder.getAllStudents();
        scanner = new Scanner(System.in);
        sortedStudentListByAverage = sortByAverageResult(allStudents);
        boolean exec = true;
        while (exec) {
            System.out.println("#########################################");
            System.out.println("ESCOLHA UMA DAS OPÇÕES ABAIXO:");
            System.out.println("1 - Listar alunos aprovados");
            System.out.println("2 - Listar alunos reprovados");
            System.out.println("3 - Listar alunos com nota máxima (nota 10)");
            System.out.println("4 - Listar o aluno com a menor nota");
            System.out.println("5 - Listar as 3 maiores notas");
            System.out.println("6 - Listar as 3 menores notas");
            System.out.println("7 - Listar médias dos alunos");
            System.out.println("x - Sair");
            System.out.println("#########################################");
            String opcao = scanner.nextLine().replaceAll("\\s+", " ").trim();
            switch (opcao) {
                case "1":
                    listApprovedStudents();
                    break;
                case "2":
                    listFailedStudents();
                    break;
                case "3":
                    listStudentsWithMaxGrade();
                    break;
                case "4":
                    listStudentWithLowestGrades();
                    break;
                case "5":
                    listTopThreeStudents();
                    break;
                case "6":
                    listBottomThreeGrades();
                    break;
                case "7":
                    listAllStudentsAverages();
                    break;
                case "x":
                    exec = false;
                    continue;
                default:
                    System.out.println("Opção inválida!");
            }
            System.out.println("\nPressione Enter para continuar...");
            scanner.nextLine();
        }
    }

    private static float getAverageStudent(Studant studant) {
        return (studant.getTestOne() + studant.getTestTwo() + studant.getTestThree()) / 3;
    }

    private static List<Studant> sortByAverageResult(List<Studant> students) {
        List<Studant> newList = new ArrayList<Studant>(students);
        for (int i = 1; i < newList.size(); i++) {
            double currentAverage = getAverageStudent(newList.get(i));
            double previousAverage = getAverageStudent(newList.get(i - 1));
            if (currentAverage > previousAverage) {
                Studant currentStudent = newList.get(i);
                newList.remove(i);
                newList.add(i - 1, currentStudent);
                i = Math.max(0, i - 2);
            }
        }
        return newList;
    }


    /**
     * 1. Recupere da lista os alunos que passaram de ano (nota minima 7.0).
     * - Exiba os dados nesse formato: <código> - <nome> : Média = <nota>
     */
    public static void listApprovedStudents() {
        List<Studant> approved = new ArrayList();
        for (Studant studant : sortedStudentListByAverage) {
            float media = getAverageStudent(studant);
            if (getAverageStudent(studant) >= 7.0) {
                System.out.printf("%d - %s : Média = %f\n", studant.getCode(), studant.getName(), media);
            }
        }
    }

    /**
     * 2. Recupere da lista os alunos que não passaram de ano.
     * - Exiba os dados nesse formato: <código> - <nome> : Média = <media> (Faltou = <nota_faltante>)
     */
    public static void listFailedStudents() {
        for (Studant studant : sortedStudentListByAverage) {
            float media = getAverageStudent(studant);
            if (getAverageStudent(studant) < 7.0) {
                System.out.printf("%d - %s : Média = %f  (Faltou = %f)\n", studant.getCode(), studant.getName(), media, 7.0 - media);
            }
        }
    }

    /**
     * 3. Traga os alunos que tiraram a nota máxima (nota 10).
     * - Exiba os dados nesse formato: <código> - <nome>
     */
    public static void listStudentsWithMaxGrade() {
        boolean t = false;
        for (Studant studant : sortedStudentListByAverage) {
            float media = getAverageStudent(studant);
            if (media == 10) {
                System.out.printf("%d - %s\n", studant.getCode(), studant.getName());
                t = true;
            }
        }
        if (!t)
            System.out.println("Nenhum aluno tirou a nota máxima.");
    }

    /**
     * 4. Traga o aluno que tirou a menor nota, em caso de notas iguais, traga ambos os alunos.
     * - Exiba os dados nesse formato: <código> - <nome> : Nota = <nota>
     */
    public static void listStudentWithLowestGrades() {
        Studant last = null;
        for (int i = sortedStudentListByAverage.size() - 1; i >= 0; i--) {
            Studant studant = sortedStudentListByAverage.get(i);
            float media = getAverageStudent(studant);
            if (i == sortedStudentListByAverage.size() - 1 && media < 7) {
                last = studant;
                System.out.printf("%d - %s : Nota = %f\n", studant.getCode(), studant.getName(), media);
            } else if (last != null && media == getAverageStudent(last)) {
                System.out.printf("%d - %s : Nota = %f\n", studant.getCode(), studant.getName(), media);
            }
        }
    }

    /**
     * 5. Faça uma lista com top 3 notas de alunos. Em caso de notas iguais coloque todos na mesma posição.
     * - Ex:
     * 1º - Fulano : Nota = 10.0;
     * - Beltrano : Nota = 10.0;
     * 2º - Joãozinho : Nota = 9.0;
     * 3º - Mariazinha : Nota = 8.9;
     * - Exiba os dados nesse formato: <posicao> - <nome> : Nota = <nota>
     */
    public static void listTopThreeStudents() {
        int position = 1;
        for (var i = 0; i < sortedStudentListByAverage.size(); i++) {
            Studant studant = sortedStudentListByAverage.get(i);
            float media = getAverageStudent(studant);
            if (i == 0) {
                System.out.printf(" %dº - %s : Nota = %f\n", position, studant.getName(), media);
            } else if (position <= 3 && getAverageStudent(sortedStudentListByAverage.get(i - 1)) == media) {
                System.out.printf("    - %s : Nota = %f\n", studant.getName(), media);
            } else if (position < 3) {
                position++;
                System.out.printf(" %dº - %s : Nota = %f\n", position, studant.getName(), media);
            } else {
                break;
            }
        }
    }

    /**
     * 6. Faça uma lista com as 3 menores notas de alunos. Em caso de notas iguais coloque todos na mesma posição. Exemplo igual a anterior
     * - Exiba os dados nesse formato: <posicao> - <nome> : Nota = <nota>
     */
    public static void listBottomThreeGrades() {
        int position = 1;
        for (var i = sortedStudentListByAverage.size() - 1; i >= 0; i--) {
            Studant studant = sortedStudentListByAverage.get(i);
            float media = getAverageStudent(studant);
            if (i == sortedStudentListByAverage.size() - 1) {
                System.out.printf("%dº - %s : Nota = %f\n", position, studant.getName(), media);
            } else if (position <= 3 && i >= 0 && getAverageStudent(sortedStudentListByAverage.get(i + 1)) == media) {
                System.out.printf("     %s : Nota = %f\n", studant.getName(), media);
            } else if (position < 3) {
                position++;
                System.out.printf("%dº - %s : Nota = %f\n", position, studant.getName(), media);
            } else {
                break;
            }
        }
    }

    /**
     * 7. Monte a média de todos os alunos e exiba em tela ordenando da maior para a menor nota.
     * - Exiba os dados nesse formato: <posicao> - <código> - <nome> : Média = <nota>
     */
    public static void listAllStudentsAverages() {
        int position = 0;
        for (Studant studant : sortedStudentListByAverage) {
            position++;
            float media = getAverageStudent(studant);
            System.out.printf("%d - %d - %s : Média = %f\n", position, studant.getCode(), studant.getName(), media);
        }
    }
}
