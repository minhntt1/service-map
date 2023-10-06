package com.example.demo.util;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@JsonComponent
public class SecondaryStatSerializer extends JsonSerializer<Double>{
	@Override
	public void serialize(Double arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
		// TODO Auto-generated method stub
		arg1.writeString(new StringBuilder().append(arg0).append(" req/m").toString());
	}
}
