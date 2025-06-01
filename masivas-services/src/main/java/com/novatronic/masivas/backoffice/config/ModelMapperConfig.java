package com.novatronic.masivas.backoffice.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.Converter; // Importa Converter
import org.modelmapper.spi.MappingContext; // Importa MappingContext
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat; // Para el formato de fecha
import java.util.Calendar;
import java.util.Date; // Para el tipo Date

@Configuration
public class ModelMapperConfig {

//    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"; // Define tu formato deseado
    private static final String DATE_FORMAT = "dd/MM/yyyy hh:mm:ss"; // Define tu formato deseado

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Configura el convertidor para Date a String
        Converter<Date, String> dateToStringConverter = (MappingContext<Date, String> context) -> {
            Date source = context.getSource();
            if (source == null) {
                return null;
            }
            // --- ¡La mejora clave aquí! ---
            // Truncar los milisegundos a cero usando Calendar
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(source);
            calendar.set(Calendar.MILLISECOND, 0); // Establece los milisegundos a 0
            Date dateWithoutMillis = calendar.getTime(); // Obtiene la Date truncada

            // Formatea la fecha truncada a String
            return new SimpleDateFormat(DATE_FORMAT).format(dateWithoutMillis);

            // Formatea la fecha a String
//            return new SimpleDateFormat(DATE_FORMAT).format(source);
        };

        // Añade el convertidor al ModelMapper.
        // Esto le dice a ModelMapper que use este convertidor cada vez que intente mapear un Date a un String.
        modelMapper.addConverter(dateToStringConverter);

        return modelMapper;
    }
}
