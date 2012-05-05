package trabed2;

/*
 * Esta classe representa a implementa��o da 
 *   �rvore bin�ria. Seu �nico atributo � uma 
 *   refer�ncia para o n� raiz. Todos os outros
 *   n�s ser�o obtidos por meio de fun��es que
 *   os buscam nas sub-�rvores esquerda e direita
 *   da raiz.
 */
public class BinaryTree {
	private Node root;          //raiz da �rvore bin�ria
	
	//Por hip�tese, a �rvore, quando criada, est� vazia.
	public BinaryTree(){
		root = null;
	}
	
	//M�todo que informa se a �rvore est� vazia (sem n�s)
	//  ou n�o. Retorna TRUE se a �rvore estiver vazia
	//  ou FALSE, caso contr�rio.
	public boolean emptyTree(){
		return (root == null);
	}
	
	//M�todo que muda a raiz da �rvore.
	public void setRoot (Node n){
		root = n;
	}
	
	//M�todo que recupera a raiz da �rvore
	public Node getRoot (){
		return root;
	}
	
	//M�todo de inser��o de um n� na �rvore bin�ria
	public void insertNode (int k){
		
	}
	
	//M�todo que insere um n� 'k' na sub-�rvore
	//  cuja raiz � 'current'.
	public void insert(Node current, Node k){
		//Verifica se 'k' deve ficar na sub-�rvore
		//  esquerda ou direita de 'current'. Em
		//  seguida chama recursivamente a fun��o
		//  para inserir 'n' na posi��o mais 
		//  adequada.
            if (emptyTree()){
			root = k;
		}
            else{
		if (current.getKey() < k.getKey()){
			if (current.getRight() != null){
				insert (current.getRight(), k);
			}
			else{
				current.setRight(k);
			}
		}
		else {
			if (current.getLeft() != null){
				insert (current.getLeft(), k);
			}
			else{
				current.setLeft(k);
			}
		}
            }
		
		return;
	}
	

        public void emOrdem(Node no) {
            if(no != null) {
                emOrdem(no.getLeft());
                System.out.print(no.getKey()+" ");
                emOrdem(no.getRight());
            }
        }
}
