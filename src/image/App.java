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

        
        // Image.imWrite(imLogica, pathResult+"test2.jpg");
    }
}