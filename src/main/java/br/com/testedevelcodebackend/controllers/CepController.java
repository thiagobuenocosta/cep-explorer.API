package br.com.testedevelcodebackend.controllers;

import br.com.testedevelcodebackend.model.Cep;
import br.com.testedevelcodebackend.repository.CepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/cep")
public class CepController {
    private List<Cep> ceps = new ArrayList<>();

    @Autowired
    private CepRepository cepRepository;

    @CrossOrigin
    @GetMapping("/")
    public List<Cep> cepList() {
        return cepRepository.findAll();
    }

    @CrossOrigin
    @PostMapping("/")
    public ResponseEntity<Cep> cepAdd(@RequestBody Cep cep) {
        String cepFound = this.cepSearch(cep.getCepNumber());

        if (cepFound.indexOf("400") != -1)
            return new ResponseEntity<>(cep, HttpStatus.BAD_REQUEST);

        if (cepFound.indexOf("erro") != -1)
            return new ResponseEntity<>(cep, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(this.cepRepository.save(cep), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/searchCep/{cep}")
    public String cepSearch(@PathVariable("cep") String cep) {
        String json;

        try {
            URL url = new URL("http://viacep.com.br/ws/"+ cep +"/json");
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonSb = new StringBuilder();

            bufferedReader.lines().forEach(l -> jsonSb.append(l.trim()));
            System.out.println(urlConnection);
            json = jsonSb.toString();

            Map<String,String> mapa = new HashMap<>();

            Matcher matcher = Pattern.compile("\"\\D.*?\": \".*?\"").matcher(json);
            while (matcher.find()) {
                String[] group = matcher.group().split(":");
                mapa.put(group[0].replaceAll("\"", "").trim(), group[1].replaceAll("\"", "").trim());
            }

            System.out.println(mapa);
            return json;
        } catch (Exception e) {
            return e.getMessage();
        }


    }


}
