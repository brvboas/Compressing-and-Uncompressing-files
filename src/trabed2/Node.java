package trabed2;


public class Node {

    private int key;                       
    private char caracter;
    private String codificacao;
    private Node left;                     
    private Node right;                   

    //Construtor
    public Node(int k, char c) {
        key = k;
        caracter = c;
        codificacao = "";
        left = right = null;

    }

    public Node() {
        key = 0;
        caracter = ' ';
        codificacao = "";
        left = right = null;

    }

  
    public boolean leaf() {
        return (left == null && right == null);
    }

    public void setKey(int k) {
        key = k;
    }

    public int getKey() {
        return key;
    }

    public void setLeft(Node n) {
        left = n;
    }

    public Node getLeft() {
        return left;
    }

    public void setRight(Node n) {
        right = n;
    }

    public Node getRight() {
        return right;
    }

    public char getCaracter() {
        return caracter;
    }

    public void setCaracter(char caracter) {
        this.caracter = caracter;
    }

    public String getCodificacao() {
        return codificacao;
    }

    public void setCodificacao(String codificacao) {
        this.codificacao = codificacao;
    }

    @Override
    public String toString() {
        return ( caracter + ": " +Integer.toString(key));
    }

    private void codificaNode(String path) {
        if ((left == null) && (right == null)) {

            codificacao = path;
            //System.out.println(caracter + ": " + codificacao);
        }

        if (left != null) {
            left.codificaNode(path + '1');
        }
        if (right != null) {
            right.codificaNode(path + '0');
        }
    }

    public static void geraCodificacao(Node tree) {
        tree.codificaNode("");
    }
}
