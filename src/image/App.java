package image;
import image.processing.Image;

public class App {
    public static void main(String[] args) throws Exception {
        // Variáveis com os diretórios
        final String
            pathOriginals = "src/image/originais/",
            pathResults = "src/image/resultantes/";
        // Total de colunas da pilha e cabeça na imagem
        int totalColunasPilha, totalColunasCabeca;
        // A variável métrica diz respeito ao tamanho da pilha em mm
        float
            metrica = (float) 6.8,
            diametroPilha;
        
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
        
        var imLogica = Image.bwClose(
            Image.logical(imGray),
            6
        );

        imLogica = Image.bwOpen(
            imLogica,
            15
        );

        /*
         * Criando um vetor booleano do tamanho da largura
         * da imagem para que vejamos quais colunas possuem
         * pixels de foreground.
         */
        var fgPixels = new boolean[imLogica[0].length];
        for(int i = 0; i < imLogica.length; i++)
            for(int j = 0; j < imLogica[0].length; j++)
                if(imLogica[i][j] && !fgPixels[j])
                    fgPixels[j] = true;
        
        // Guardando índices de início e de fim das colunas correspondentes a pilha
        int inicioPilha = -1, fimPilha = -1;
        for(int i = fgPixels.length-1; i >= 0; i--){
            if(fgPixels[i]){
                if(inicioPilha == -1)
                    inicioPilha = i;
                if(fgPixels[i-1] == false){
                    fimPilha = i;
                    break;
                }
            }
        }
        
        totalColunasPilha = inicioPilha-fimPilha+1;

        // Criando imagem com apenas a pilha a partir dos indices obtidos anteriormente
        var pilha = new boolean[imLogica.length][totalColunasPilha];
        for(int i = 0; i < pilha.length; i++)
            for(int j = 0; j < pilha[0].length; j++)
                pilha[i][j] = imLogica[i][j+fimPilha];
        
        // Guardando índices de início e de fim das colunas correspondentes a cabeça
        int inicioCabeca = -1, fimCabeca = -1;
        for(int i = 0; i < fgPixels.length; i++){
            if(fgPixels[i]){
                if(inicioCabeca == -1)
                    inicioCabeca = i;
                if(fgPixels[i+1] == false){
                    fimCabeca = i;
                    break;
                }
            }
        }

        totalColunasCabeca = fimCabeca-inicioCabeca+1;

        // Criando imagem com apenas a cabeça a partir dos indices obtidos anteriormente
        var cabeca = new boolean[imLogica.length][totalColunasCabeca];
        for(int i = 0; i < cabeca.length; i++)
            for(int j = 0; j < cabeca[0].length; j++)
                cabeca[i][j] = imLogica[i][j+inicioCabeca];
        
        Image.imWrite(pilha, pathResults+"pilha.png");
        Image.imWrite(imLogica, pathResults+"mascara.png");
        Image.imWrite(Image.logical(imGray), pathResults+"logica.png");
    }
}