package com.port.entity;

/**
 * Enum of attribute Type.
 */
public enum EnumType {

    T1("COD", 8),
    T2("EMS", 3);

    private String key;

    private Integer value;

    /**---------------------------------------------------------------------------
     * Private constructor for EnumAttributeType.
     * @param key for get value
     * @param value of EnumAttributeType
     */
    private EnumType(final String key, final Integer value) {
        this.setKey(key);
        this.setValue(value);
    }

    /************************************************************************
     * <b>Description:</b><br>
     *  get value of enum type by key.
     *
     * @author		Nguyen.Chuong
     * @date		Jan 15, 2014
     * @param       key to get value. Can be: String, integet, group,...
     * @return		String
     ************************************************************************/
    public static Integer getTypeByKey(String key) {
        for (EnumType valueEnum : values()) {
            if (key.equals(valueEnum.getKey())) {
                return valueEnum.getValue();
            }
        }
        return null;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the value
     */
    public Integer getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Integer value) {
        this.value = value;
    }

}
