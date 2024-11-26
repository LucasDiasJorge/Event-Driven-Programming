package com.service.sse.enums.base;

public interface EnumOptionBase {

    public String getEnumName();
    public String getValue();
    public Boolean enableSerialization();

    public static <E extends Enum<E> & EnumOptionBase> E fromValue(Class<E> enumClass, String value) {
        for (E constant : enumClass.getEnumConstants()) {
            if (constant.getValue().equalsIgnoreCase(value) || constant.name().equalsIgnoreCase(value)) {
                return constant;
            }
        }
        throw new EnumConstantNotPresentException(enumClass,"No Constant found for value '" + value + "' in '" + enumClass.getSimpleName() + "'");
    }

}