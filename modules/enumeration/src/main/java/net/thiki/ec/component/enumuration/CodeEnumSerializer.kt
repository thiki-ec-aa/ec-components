package net.thiki.ec.component.enumuration

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

class CodeEnumSerializer<E: ICode>(type: Class<E>): StdSerializer<E>(type) {
    override fun serialize(value: E, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeNumber(value.code)
    }
}