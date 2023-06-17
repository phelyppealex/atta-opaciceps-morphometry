package image;
import image.processing.Image;

public class App {
    public static void main(String[] args) throws Exception {
        // Variáveis com os diretórios
        final String
            pathOriginals = "src/image/originais/",
            pathResult = "src/image/resultantes/";

        // Lendo banco de imagens
        var imRGB = Image.imRead(pathOriginals + "1.jpg");

        // Convertendo a imagem para tons de cinza
        var imGray = Image.rgb2gray(imRGB);

        /*
         * Fazendo a limiarização da imagem a partir
         * de uma intensidade constante encontrada
         */
        for(int i = 0; i < imGray.length; i++)
            for(int j = 0; j < imGray[0].length; j++)
                if(imGray[i][j] < 110)
                    imGray[i][j] = 1;
                else
                    imGray[i][j] = 0;
        
        /*
         * Aqui a imagem se torna lógica com a função "logical" e,
         * logo após, a imagem é erodida e dilatada para remover ruídos.
         * A variável "imLogica recebe o resultado desse procedimento."
         */
        var imLogica = Image.bwOpen(
            Image.logical(imGray),
            11
        );

        /*
         * Criando um vetor booleano do tamanho da largura
         * da imagem para que vejamos quais colunas possuem
         * pixels de foreground.
         */
        var somaPixels = new boolean[imLogica[0].length];
        for(int i = 0; i < imLogica.length; i++)
            for(int j = 0; j < imLogica[0].length; j++)
                if(imLogica[i][j])
                    somaPixels[j] = true;
        
        int inicioPilha = -1, fimPilha = -1;
        for(int i = somaPixels.length-1; i >= 0; i--){
            if(somaPixels[i]){
                if(inicioPilha == -1)
                    inicioPilha = i;
                if(somaPixels[i-1] == false){
                    fimPilha = i;
                    break;
                }
            }
        }

        var pilha = new boolean[imLogica.length][inicioPilha-fimPilha+1];

        for(int i = 0; i < pilha.length; i++)
            for(int j = 0; j < pilha[0].length; j++)
                pilha[i][j] = imLogica[i][j+fimPilha];
        
        Image.imWrite(pilha, pathResult+"pilha.jpg");
        //Image.imWrite(imLogica, pathResult+"teste2.jpg");
    }
}