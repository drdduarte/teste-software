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


//    @Test
//    public void buscandoComPostTest() {
//        Response response = given()
//                .when()
//                .body("JMeter")
//                .post("/buscaLancamentos");
//
//        assertEquals(response.getStatusCode(), 200);
//
//        InputStream in = response.asInputStream();
//        List<Lancamento> list = JsonPath.with(in)
//                .getList("lancamentos", Lancamento.class);
//        assertEquals(list.size(), 10);
//    }

    public BigDecimal buscaJSONPath(String valor){
//        Response response = get("/buscaLancamentos");
//        String lowestNumberPlayer = response.path("players.min { it.jerseyNumber }.name");
//        System.out.println(lowestNumberPlayer);

        Response response = given()
                .when()
                //.body(valor)
                .post("/buscaLancamentos");


        StringToMoneyConverter converter = new StringToMoneyConverter();
        InputStream in = response.asInputStream();
        List<Lancamento> list = JsonPath.with(response.getBody().asInputStream())
                .getList("lancamentos.findAll{}", Lancamento.class);
        //.getList("lancamentos.findAll{l -> l.valor < 9999999}", Lancamento.class);
        //.getList("lancamentos.max { it.valor }.valor", Lancamento.class);
//        String highestNumberPlayer = response.path("lancamentos.max { it.valor }.name");
        //String highestNumberPlayer = response.path("players.max { it.jerseyNumber }.name");
        BigDecimal valorMinimo = new BigDecimal(9999999);
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



//        for(Lancamento lancamento: list){
//            if ( lancamento.getValor().compareTo(valorMinimo) < 0 ){
//                valorMinimo = lancamento.getValor();
//            }
//        }
//        System.out.println(">>>>>>>>valorMinimo: "+valorMinimo);
//
//        for(Lancamento lancamento: list){
//
//            //for (int i = 0; i < list.size(); i++) {
////            System.out.println(">>>>>>>>> "+list.get(i));
////            Object obj = list.get(i);
//            //if (list.get(i).getId(). getItemName().equals("")) {
//            //LancamentoVO lancamento = (LancamentoVO) list.get(i);
//            System.out.println(">>>>>>>>VALOR: "+lancamento.getValor());
//        }
        //assertEquals(list.size(), 1);
//        BigDecimal valor = converter.convert(JsonPath.with(in).get("valor").toString());


    }

    public BigDecimal buscaListaJava(String valor){
        Response response = given()
                .when()
                //.body(valor)
                .post("/buscaLancamentos");
        StringToMoneyConverter converter = new StringToMoneyConverter();
        InputStream in = response.asInputStream();
        List<Lancamento> list = JsonPath.with(response.getBody().asInputStream())
                .getList("lancamentos.findAll{}", Lancamento.class);
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

    @Test
    public void executarTestes(){
        //BigDecimal retornoJSONPath = buscaJSONPath("REST");
        BigDecimal retornoListaJava = buscaListaJava("REST");
        //assertEquals(retornoJSONPath, retornoListaJava, "Comparação das duas validações de menor valor");
        Boolean valida = false;
    }
}