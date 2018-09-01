package br.ucdb.pos.engenhariasoftware.testesoftware.controller;

import br.ucdb.pos.engenhariasoftware.testesoftware.controller.vo.LancamentoVO;
import br.ucdb.pos.engenhariasoftware.testesoftware.converter.StringToMoneyConverter;
import br.ucdb.pos.engenhariasoftware.testesoftware.modelo.Lancamento;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class LancamentoControllerTest {

    @Test
    public void executarTestes(){
//        Caso seja necessário dar a carga inicial para cada teste, poderá utlizar uma das quadras chamadas abaixo;
//        cargaTeste(1);
//        cargaTeste(2);
//        cargaTeste(3);
//        cargaTeste(4);

        BigDecimal retornoJSONPath = buscaJSONPath(" ");
        BigDecimal retornoListaJava = buscaListaJava(" ");
        assertEquals(retornoJSONPath, retornoListaJava, "Comparação das duas validações de menor valor");
        Boolean valida = false;

        // CONCLUSAO
        // - Realizando essa validação via código java, me pareceu muito mais segura e integra
        //   devido aos diversos recursos que a linguagem proporciona. Já tentando utilizar apenas o JSONPath,
        //   eu não conseguir realziar o teste como deveria, e o fato de realizarmos filtros tudo por uma concatenação
        //   de string, fica sujeito a erro de digitação e bugs, particularmente não gosto de utilizar dessa forma,
        //   porém pode ser que em algumas situação pode ajudar bastante.
    }

    /**
     * Metodo de Busca RestAssured - Valição JAVA
     * */
    public BigDecimal buscaListaJava(String filtro){
        Response response = given()
                .when()
                .body(filtro)
                .post("/buscaLancamentos");
        InputStream in = response.asInputStream();
        List<Lancamento> list = JsonPath.with(in)
                .getList("lancamentos", Lancamento.class);
        BigDecimal valorMinimo = new BigDecimal(9999999);
        // CASO 01
        if(list.size()==1) {
            valorMinimo = list.get(0).getValor();
        }
        else{ // CASOS 02, 03, 04
            for(Lancamento lancamento: list){
                if (lancamento.getValor().compareTo(valorMinimo) < 0 ){
                    valorMinimo = lancamento.getValor();
                }
            }
        }
        return valorMinimo;
    }

    /**
     * Metodo de Busca RestAssured - JSONPath
     * */
    public BigDecimal buscaJSONPath(String filtro){
        Response response = given()
                .when()
                .body(filtro)
                .post("/buscaLancamentos");
        StringToMoneyConverter converter = new StringToMoneyConverter();
        InputStream in = response.asInputStream();
        BigDecimal valorMinimo = new BigDecimal("99999.00");
        //String urlJSONPath = "lancamentos.findAll{l -> l.valor < "+valorMinimo.toString()+"}";
        String urlJSONPath = "lancamentos.findAll{l -> l.id > 0}";
        List<Lancamento> list = JsonPath.with(in).getList(urlJSONPath, Lancamento.class);

//        List<?> list = JsonPath.with(response.getBody().asInputStream())
//                .getList(urlJSONPath, List.class);
        //.getList("lancamentos.findAll{l -> l.valor < 9999999}", Lancamento.class);
        //.getList("lancamentos.max { it.valor }.valor", Lancamento.class);
//        String highestNumberPlayer = response.path("lancamentos.max { it.valor }.name");
        //String highestNumberPlayer = response.path("players.max { it.jerseyNumber }.name");
//        for (int i = 0; i < list.size(); i++) {
//            //System.out.println(">>>>>>>>LINHA: "+list.get(i).getValor());
//            System.out.println(">>>>>>>>LINHA: "+list.get(i));
//            String valorTMP = JsonPath.with((String) list.get(i)).get("").toString();
//            System.out.println(">>>>>>>>VALOR: "+valorTMP);
//        }
        // CASO 01
        if(list.size()==1) {
            valorMinimo = list.get(0).getValor();
        }
        else{ // CASOS 02, 03, 04
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).getValor().compareTo(valorMinimo)<0){
                    valorMinimo = list.get(i).getValor();
                }
            }
        }
        return valorMinimo;
    }

    public void cargaTeste(Integer caso){
        LancamentoVO lancamentoVO;
        if(caso.equals(1)){ // Lista de lançamentos de tamanho 1;
            lancamentoVO = new LancamentoVO(
                    1,
                    "Assured Rest Caso 01",
                    "50,00",
                    "01/09/2018",
                    "ENTRADA",
                    "SALARIO"
                    );
            insereDados(lancamentoVO);
        }
        else if(caso.equals(2)){ // Lista de lançamentos tamanho 2, com valores distintos e positivos;
            lancamentoVO = new LancamentoVO(
                    1,
                    "Assured Rest Caso 02 - 1",
                    "120,00",
                    "01/09/2018",
                    "ENTRADA",
                    "SALARIO"
            );
            insereDados(lancamentoVO);
            lancamentoVO = new LancamentoVO(
                    2,
                    "Assured Rest Caso 02 - 2",
                    "230,00",
                    "01/09/2018",
                    "ENTRADA",
                    "SALARIO"
            );
            insereDados(lancamentoVO);
        }
        else if(caso.equals(3)){ // Lista de lançamentos tamanho 2, com valores distintos e negativos;
            lancamentoVO = new LancamentoVO(
                    1,
                    "Assured Rest Caso 03 - 1",
                    "20,00",
                    "01/09/2018",
                    "ENTRADA",
                    "SALARIO"
            );
            insereDados(lancamentoVO);
            lancamentoVO = new LancamentoVO(
                    2,
                    "Assured Rest Caso 03 - 2",
                    "30,00", // não foi possível inserir dados negativos, aparentemente a aplicação não deixa
                    "01/09/2018",
                    "ENTRADA",
                    "SALARIO"
            );
            insereDados(lancamentoVO);
        }
        else if(caso.equals(4)){ // Lista de lançamentos tamanho > 5, com valores iguais ou distintos, positivos e ou negativos.
            lancamentoVO = new LancamentoVO(
                    1,
                    "Assured Rest Caso 04 - 1",
                    "20,00",
                    "01/09/2018",
                    "ENTRADA",
                    "CARRO"
            );
            insereDados(lancamentoVO);
            lancamentoVO = new LancamentoVO(
                    2,
                    "Assured Rest Caso 04 - 2",
                    "30,00",
                    "01/09/2018",
                    "ENTRADA",
                    "SALARIO"
            );
            insereDados(lancamentoVO);
            lancamentoVO = new LancamentoVO(
                    3,
                    "Assured Rest Caso 04 - 3",
                    "30,00",
                    "01/09/2018",
                    "ENTRADA",
                    "SALARIO"
            );
            insereDados(lancamentoVO);
            lancamentoVO = new LancamentoVO(
                    4,
                    "Assured Rest Caso 04 - 4",
                    "30,00",
                    "01/09/2018",
                    "ENTRADA",
                    "SALARIO"
            );
            insereDados(lancamentoVO);
            lancamentoVO = new LancamentoVO(
                    5,
                    "Assured Rest Caso 04 - 5",
                    "530,00",
                    "01/09/2018",
                    "SAIDA",
                    "CARRO"
            );
            insereDados(lancamentoVO);
        }
    }
    /**
     * Insercao de Dados
     * */
    public Integer insereDados(LancamentoVO lancamentoVO){
        Response response = given().when()
                .formParam("descricao", lancamentoVO.getDescricao())
                .formParam("valor", lancamentoVO.getValor())
                .formParam("dataLancamento", lancamentoVO.getDataLancamento())
                .formParam("tipoLancamento", lancamentoVO.getTipoLancamento())
                .formParam("categoria", lancamentoVO.getCategoria())
                .header("Content-Type", "application/x-www-form-urlencoded") // opcional
                .post("/salvar");
        return response.getStatusCode();
    }
}