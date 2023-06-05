import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;



public class Main {
    public static final String filePath = "/home/gabriel/IdeaProjects/projetoTeste/src/entries/entradaFinal.csv";
    public static final int FINAL_NODE = 100;
    public static final int FIRST_NODE = 1;
    public static boolean invalidLink = false;
    public static String execStatus = "running";
    public static int value = 0;
    public static int nextNode;
    public static int maxFlux = 0;
    public static int minimizedValue = 0;
    public static int theId;
    public static ArrayList<Integer> ids = new ArrayList<Integer>(); //Lista dos index que serão modificados

    public static ArrayList<Integer> tracedPath = new ArrayList<Integer>();

    public static void main(String[] args) {
        ArrayList<Link> links = new ArrayList<>();
        links = loadData(links);

        tracedPath.add(FIRST_NODE);

        do{
            checkStartAndEnd(links);
            tracePath(links,FIRST_NODE);
            refreshLinks(links);
        }while(execStatus != "finished");

        System.out.println("Resultado final do fluxo: "+maxFlux);
    }

    public static void checkStartAndEnd(ArrayList<Link> links){
        int startPossiblePaths = 0;
        int endPossiblePaths = 0;

        for (Link l :links) {
            if(l.getFrom() == FIRST_NODE && !l.isDisabled())
                startPossiblePaths ++;

            if(l.getTo() == FINAL_NODE && !l.isDisabled())
                endPossiblePaths++;
        }

        if (startPossiblePaths == 0 || endPossiblePaths == 0)
            execStatus = "finished";
    }

    public static void tracePath(ArrayList<Link> links, int currentNode){

        ArrayList<Link> tempList = new ArrayList<>(); // Lista que vai conter apenas os links do Node atual

        value = 0;
        nextNode = FIRST_NODE;
        theId = -1;

        int y = 0;
        for (Link l :links){
            if(l.isDisabled())
                y++;
            if(y == links.size()){
                System.out.println("Todos os nodos desabilitados!");
                execStatus = "finished";
                return;
            }
        }

        for (Link l :links){ //Se o get from for igual ao último nodo e não estiver desabilitado, adiciona na lista Temp
            if(l.getFrom() == currentNode && !l.isDisabled()){
                tempList.add(l);
            }
        }

        if(tempList.isEmpty()){// se não houver caminhos pula fora. Motivos(Final do grafo ou caminhos desabilitados)
            if(currentNode == FINAL_NODE){
                System.out.println("Final do Grafo");
                System.out.println("Valor de minimização: "+minimizedValue+"\n---------------------");
            }
            else {
                System.out.println("Não há arestas válidas, todas possíveis desabilitadas, return!\n---------------------");
                inactivateLinks(links, currentNode);
                invalidLink = true;
            }
            return;
        }

        int x = 0;
        for (Link l :tempList){ //verifica se os próximos nodos possíveis da lista temporária estão no tracedPath
            if(tracedPath.contains(l.getTo()))
                x++;
            if(tempList.size() == x){
                System.out.println("Não há arestas válidas, todas no tracedPath! return\n---------------------");
                inactivateLinks(links, currentNode);
                invalidLink = true;
                return;
            }
        }

        for (Link l :tempList){ //Verifica os links e se o valor for maior que o link anterior, assume estes valores (percorrer o maior caminho)
            if (l.getValue()>value && !tracedPath.contains(l.getTo())){ //Segunda comparação verifica se o nodo já não foi usado nesta iteração
                value = l.getValue();
                nextNode = l.getTo();
                theId = l.getIndexInList();
            }
        }

        if(theId != -1){ ids.add(theId); }

        if(theId != -1)
            System.out.println("Nodo Atual:"+links.get(theId).getFrom()+" Valor:"+value+"| proximo Nodo:"+nextNode+" | ID in List:"+ids.get(ids.size()-1));


        if(currentNode == FIRST_NODE && tempList.size()==1){ //se for a primeira iteração e tiver apenas um item no tempList, assume o valor
            minimizedValue = value;
        }
        else if((currentNode == FIRST_NODE) && links.get(ids.get(0)).getValue() == value){ //se for a primeira iteração, o valor de minimização assume o value atual
            minimizedValue = value;
        }
        else if(minimizedValue > value && value > 0){ //se não, verifica se o valor atual é menor que a minimização, se for, assume ele
            minimizedValue = value;
        }

        if (nextNode != FIRST_NODE){ //se não chegamos no final do grafo, faz uma chamada recursiva para essa função
            tracedPath.add(nextNode);
            tracePath(links,nextNode);
        }
    }

    public static void inactivateLinks(ArrayList<Link> links, int currentNode){ //Desabilita todos as arestas que levam para o nodo sem saída
        for (Link l :links){
            if(l.getTo() == currentNode){
                l.setDisabled(true);
                System.out.println("Desativando Link:"+l.getFrom()+" -> "+l.getTo()+" | Residual Value:"+l.getValue());
            }
        }
    }

    public static void refreshLinks(ArrayList<Link> links){
        int tempValue = 0;
        for(Integer id :ids) {
            if (!invalidLink){
                tempValue = links.get(id).getValue() - minimizedValue;
                links.get(id).setValue(tempValue);

                if (links.get(id).getValue() == 0) {
                    links.get(id).setDisabled(true);
                }
            }
        }
        ids.removeAll(ids);

        if(!invalidLink && tracedPath.get(tracedPath.size()-1)==FINAL_NODE)
            maxFlux += minimizedValue;

        invalidLink = false;
        minimizedValue = 0;
        tracedPath.removeAll(tracedPath);
        tracedPath.add(FIRST_NODE);
    }

    public static ArrayList loadData(ArrayList<Link> links) {
        String [] temp;
        File file = new File(filePath);
        Scanner sc = null;

        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        while (sc.hasNextLine()){
            temp = sc.nextLine().split(",");
            Link tempLink = new Link(Integer.parseInt(temp[0]),Integer.parseInt(temp[1]),Integer.parseInt(temp[2]), links.size());
            links.add(tempLink);
        }
        return links;
    }
}


