package com.example.demo.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class SubtitleSerializer extends JsonSerializer<Double>{
	@Override
	public void serialize(Double arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
		// TODO Auto-generated method stub
		arg1.writeString(new StringBuilder().append("Error: ").append(arg0*100).append("%").toString());
	}
}
