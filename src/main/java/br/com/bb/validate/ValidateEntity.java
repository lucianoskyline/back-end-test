package br.com.bb.validate;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luciano on 31/01/2019.
 */
public class ValidateEntity {

    public List<String> showErros(BindingResult bindingResult){
        List<String> erros=new ArrayList<>();

        for(ObjectError obj:bindingResult.getAllErrors()){
            erros.add(obj.getDefaultMessage());
        }

        return erros;
    }

}
