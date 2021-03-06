/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trabed2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Vector;

/**
 *
 * @author Takeshi
 */
public class Huffman {

    /**
     * @param args the command line arguments
     */
    private static long lengthInicial = 0;
    private static long lengthFinal = 0;
    private static Vector<Node> caracteres = new Vector<Node>();
    private static char lineSeparator = '\n';
    private static char marcador = '~';

    public static void main(String[] args) {

        if (args.length == 0) {
            args = new String[4];
            //args[0] = "-c";
            args[0] = "-x";
            //args[1] = "e.txt";
            args[1] = "s.txt";
            args[2] = "-o";
            //args[3] = "s.txt";
            args[3] = "original.txt";

        }


        if ((args[0].equals("-c") || args[0].equals("-x")) && !args[1].equals("") && args[2].equals("-o") && !args[3].equals("")) {
            //Abri o arquivo de entrada
            BufferedReader br = null;
            String saida = "";
            try {
                br = abrir(args[1]);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
            //Compactar
            if (args[0] == null ? "-c" == null : args[0].equals("-c")) {
                try {
                    //Campactar
                    String linha;
                    String s = "";
                    //Concatena todas as linhas do arquivo em uma string soh
                    while ((linha = br.readLine()) != null) {
                        s = s + linha + lineSeparator;
                    }
                    //funcao de compactar
                    saida = compactar(s);
                    br.close();

                    //salva o arquivo de saida
                    //salva os codeword
                    String codwords="";
                    for (int i=0 ; i<caracteres.size() ; i++){
                        codwords=codwords+caracteres.get(i).getCaracter()+caracteres.get(i).getCodificacao()+marcador;
                    }

                    //salva os codwords
                    salvar(args[3], codwords+"->"+lineSeparator, false);
                    
                    //salva o arquivo compactado
                    salvar(args[3], saida, true);

                    //Relatorio de campactacao
                    System.out.println("\nRelatório de Compactação");
                    System.out.println("Tamanho do Arquivo Inicial: " + lengthInicial + " bytes.");
                    System.out.println("Tamanho do Arquivo Final: " + lengthFinal + " bytes.");
                    double a = (double) lengthFinal / (double) lengthInicial;
                    double f = (1 - a);
                    System.out.println("Taxa de Compactação: " + f);
                    System.out.println("Compactação concluida");
                    
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            } else if (args[0] == null ? "-x" == null : args[0].equals("-x")) {
                //Descompactar
                try {
                    String linha;
                    String s = "";
                    //Concatena todas as linhas do arquivo em uma string soh
                    while ((linha = br.readLine()) != null) {
                        s = s + linha + lineSeparator;
                    }
                    //funcao de descampactar
                    saida = descompactar(s);
                    br.close();

                    //salva o texto descampactado
                    salvar(args[3], saida, false);
                    System.out.println("Descompactação concluida");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
       
    }

    public static BufferedReader abrir(String arquivo) throws FileNotFoundException, IOException {
        File file = new File(arquivo);
        if (!file.exists()) {
            return null;
        }
        lengthInicial = file.length();
        FileInputStream arq = new FileInputStream(file);
        InputStreamReader lerArquivo = new InputStreamReader(arq, "ISO-8859-1");
        BufferedReader br = new BufferedReader(lerArquivo);
        return br;
    }

    public static void salvar(String arquivo, String conteudo, boolean adicionar) throws IOException {
        File file = new File(arquivo);
        Writer out = new BufferedWriter(new OutputStreamWriter(  new FileOutputStream(file,adicionar),"ISO-8859-1")   );
        out.write(conteudo);
        out.close();
        lengthFinal = file.length();
    }

    /*
     * Método de Compactação
     * Entrada = String a ser compactada
     * Saida = String Compactada
     */
    private static String compactar(String linha) {
        //String marcador = "^";
        String texto = linha;
        System.out.println("\nTexto a ser compactado: " + texto);
        texto = '^' + texto;
        Vector<Node> vetor = new Vector<Node>();

        //Pega as frequencias dos caracteres e adiciona nos vetores de Nodes
        for (int i = 0; i < texto.length(); i++) {
            Node novoNo = new Node(1, texto.charAt(i));
            if (vetor.isEmpty()) {
                vetor.add(novoNo);
                caracteres.add(novoNo);
            } else {
                boolean tem = false;
                for (int j = 0; j < vetor.size(); j++) {
                    if (novoNo.getCaracter() == vetor.get(j).getCaracter()) {
                        vetor.get(j).setKey(vetor.get(j).getKey() + 1);
                        tem = true;
                    }
                }
                if (!tem) {
                    vetor.add(novoNo);
                    caracteres.add(novoNo);
                }
            }
        }
        //System.out.println("\nFrequencias:");
        //for (int i = 0; i < vetor.size(); i++) {
            //System.out.println(vetor.get(i));
        //}


        int a = vetor.size();
        //Algoritmo de Huffman
        for (int i = 0; i < a - 1; i++) {
            Node z = new Node();
            int fx = 0, fy = 0;
            Node x = minimo(vetor);
            z.setLeft(x);
            fx = x.getKey();
            vetor.remove(x);

            Node y = minimo(vetor);
            z.setRight(y);
            fy = y.getKey();
            vetor.remove(y);

            z.setKey(fx + fy);
            vetor.add(z);
        }

        //System.out.println("\nCodewords:");
        //Gera os codwords
        Node.geraCodificacao(vetor.get(0));

        //Codifica o texto para binario
        String textoCodificado = "";
        System.out.println("Aguarde, compactando...");
        for (int i = 0; i < texto.length(); i++) {
            //if (i==196)
               // System.out.println(i);
            textoCodificado = textoCodificado + buscaCodificacao(texto.charAt(i), caracteres);
        }
        System.out.println("\nTexto codificado em binario: " + textoCodificado);

        //Adiciona 0´s a esquerda caso necessário
        if (textoCodificado.length() % 8 != 0) {
            int j = 8 - (textoCodificado.length() % 8);
            for (int i = 0; i < j; i++) {
                textoCodificado = "0" + textoCodificado;
            }
        }
        System.out.println("\nTexto codificado em binario arrumado: " + textoCodificado);

        String textoFinal = "";
        //System.out.println("\nBinario:  Decimal " + textoFinal);
        //Codifica o texto final
        for (int i = 0; i < textoCodificado.length() / 8; i++) {
            String sub = "";
            sub = textoCodificado.substring(i * 8, (i * 8) + 8);
            int codAscii = Integer.parseInt(sub, 2);
            char c = (char) codAscii;
            //System.out.println(sub + ": " + codAscii);
            if(codAscii==13)
                textoFinal = textoFinal + c +"<13>";
            else
                textoFinal = textoFinal + c;
        }
        System.out.println("\nTexto codificado final: " + textoFinal);

        return textoFinal;
    }

    /*
     * Método que retorna o Node que contem a menor frequencia de um Vector de Node
     * Entrada = Vetor de Nodes
     * Saida = Node
     */
    private static Node minimo(Vector<Node> vetor) {
        for (int i = vetor.size(); i >= 1; i--) {
            for (int j = 1; j < i; j++) {
                if (vetor.get(j - 1).getKey() > vetor.get(j).getKey()) {
                    Node aux = vetor.get(j);
                    vetor.set(j, vetor.get(j - 1));
                    vetor.set(j - 1, aux);
                }
            }
        }
        return vetor.get(0);
    }

    /*
     * Método que retorna a codificação de um caracter
     * Entrada = Caracter e Vetor de Node
     * Saida = String que contem a codificação
     */
    private static String buscaCodificacao(char c, Vector<Node> caracteres) {
        String codigo = "";
        for (int i = 0; i < caracteres.size(); i++) {
            if (caracteres.get(i).getCaracter() == c) {
                codigo = caracteres.get(i).getCodificacao();
            }
        }
        return codigo;
    }

    /*
     * Método de descampactação
     * Entrada = String a ser descompactada
     * Saida = String descampactada
     */
    private static String descompactar(String s) {
        int posChar = 0, posIniTexto = 0;
        Node novoNo = null;
        String codificacao = "";
        System.out.println("Texto a ser Descompactado: "+s);
        //Pega os codwords
        for (int i=0;i<s.length() ; i++){
            if (i==0 || s.charAt(i-1)!=marcador || s.charAt(i)!='-' || s.charAt(i+1)!='>'  ){

                if (i==0 || s.charAt(i-1)==marcador){
                    novoNo = new Node();
                    novoNo.setCaracter(s.charAt(i));
                    posChar = i+1;
                }else{
                    if (s.charAt(i)==marcador){
                        codificacao = s.substring(posChar, i);
                        novoNo.setCodificacao(codificacao);
                        caracteres.add(novoNo);
                    }
                }

            }else{
                posIniTexto = i+3;
                break;
            }
        }
        
        int valorDecimalCaracter=0;
        String textoBinario = "";
        //System.out.println("Binario:  Decimal ");
        //Converte cada caracter para seu decimal e depois o binario correspondeste

        System.out.println("Aguarde, descompactando...");
        for (int i=posIniTexto ; i<s.length()-1 ; i++ ){
            if (i==347)
                System.out.println();
            if (i!=s.length()){
                char c = s.charAt(i);
                //String prox = "";
                //prox = s.substring(i+1, i+5);
                //Converte para decimal
                if (c=='\n' && (s.substring(i+1, i+5)).equals("<13>") ){
                    valorDecimalCaracter =13;
                    i = i+4;
                }
                else
                    valorDecimalCaracter = (int) c;
                //Converte para binario
                String bin = Integer.toString(valorDecimalCaracter, 2);

                //acrescenta 0´s a esquerda caso necessite
                if (bin.length() != 8){
                    int falta = 8 - (bin.length() % 8);
                    for (int j = 0; j < falta; j++) {
                        bin = "0" + bin;
                    }
                }
                textoBinario = textoBinario + bin;
                //System.out.println(bin+": "+valorDecimalCaracter);
            }
        }
        System.out.println("\nTexto binario: "+textoBinario);

        //tira 0´s a esquerda antes do caracter marcador de inicio de Texto
        int posCorte=0;
        String codigoMarcador = buscaCodificacao('^', caracteres);
        posCorte=textoBinario.indexOf(codigoMarcador) +codigoMarcador.length();
        textoBinario=textoBinario.substring(posCorte, textoBinario.length());

        System.out.println("\nTexto binario arrumado: "+textoBinario);

        //pega o caracter correspondente ao codword
        String textoOriginal="";
        int pos=0;
        for (int i=0; i<textoBinario.length();i++){
            String bin=textoBinario.substring(pos,i);
            char caracter = buscaCaracter(bin, caracteres);
            if ( caracter != 0 ){
                pos=i;
                textoOriginal = textoOriginal + caracter;
            }
        }
        System.out.println("\nTexto descampactado: "+textoOriginal);
        return textoOriginal;
    }

    /*
     * Método de busca do caracter correspondente a um codword em um Vetor de Node
     * Entrada = String que contem o codword e Vetor de Node
     * Saida = Caracter correspondente a entrada
     */
    private static char buscaCaracter(String stringBinario, Vector<Node> caracteres) {
        char c = 0;
        for (int i=0;i<caracteres.size();i++){
            if (caracteres.get(i).getCodificacao().equals(stringBinario) )
                c = caracteres.get(i).getCaracter();
        }
        return c;
    }
}

