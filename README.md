# UC9
public class PPT {

    public static void main(String[] args) {
        Jogo Jogo = new Jogo();
        Jogo.start();
    }
}

public class Jogo {


    private int numberToGuess;
    private Jogador jogador;
    private Scanner scanner;
    private Validar validar;

    public Jogo() {
        this.numberToGuess = (int)(Math.random() * 3) + 1;
        this.jogador = new Jogador();
        this.scanner = new Scanner(System.in);
        this.validar = new Validar();
    }

    public void start() {
        System.out.println("Bem-vindo ao jogo de pedra, papel e tesoura!");
        boolean hasWon = false;

        while (!hasWon) {
            System.out.print("Digite seu palpite (1 - Pedra, 2 - Papel, 3 - Tesoura): ");
            int guess = scanner.nextInt();
            jogador.addPlays();
            hasWon = validar.validateGuess(guess, numberToGuess);
        }

        System.out.println("Parabéns, " + jogador.getName() + "! Você ganhou do computador  " + jogador.getPlays() + " jogadas!");
    }
}


public class Jogador {
   


    private String name;
    private int plays;

    public Jogador() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite seu nome:");
        this.name = scanner.nextLine();
    }

    public String getName() {
        return name;
    }

    public void addPlays() {
        this.plays++;
    }

    public int getPlays() {
        return this.plays;
    }
}

 
       
       

public class Validar {
       
    public boolean validateGuess(int guess, int numberToGuess) {
        if (guess < 1 || guess > 3) {
            System.out.println("Escolha inválida! Por favor, selecione 1 (Pedra), 2 (Papel) ou 3 (Tesoura).");
            return false;
        }

        if (guess == numberToGuess) {
            System.out.println("Empate! Ambos escolheram a mesma opção.");
            return false;
        }

        if ((guess == 1 && numberToGuess == 3) ||
            (guess == 2 && numberToGuess == 1) ||
            (guess == 3 && numberToGuess == 2)) {
            System.out.println("Você venceu!");
            return true;
        } else {
            System.out.println("Você perdeu!");
            return false;
        }
    }
}

   
 


