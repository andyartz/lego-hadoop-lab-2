package com.rwbsystems.HiveUnitTesting;

/**
 * Represents a single Hive column and the attributes associated to it like position, name, datatype, and comment.
 */
public class HiveColumnInfo {
    /**
     * The order in which the column appears in the table (0-based counter).
     */
    public int columnPosition;
    /**
     * The name of the column (case insensitive).
     */
    public String name;
    /**
     * The Hive datatype of the column (case insensitive).
     */
    public String dataType;
    /**
     * The comment for the column (case sensitive), where no comment is the empty string.
     */
    public String comment;

    /**
     *
     * @param columnPosition The position (0-based) representing the order in which the column appears.
     * @param name Column name.
     * @param dataType Column Hive data type.
     * @param comment The comment attached to the column's properties.
     */
    public HiveColumnInfo(int columnPosition, String name, String dataType, String comment) {
        this.columnPosition = columnPosition;
        this.name = name;
        this.dataType = dataType;
        this.comment = comment;
    }

    /**
     * Compares two HiveColumnInfo objects to check if they are equal.
     * @param compareTo The HiveColumnInfo object to compare the current object against.
     * @return {@code true} if the objects are the same, {@code false}, otherwise.  Case insensitive checks are on the
     * name and datatype attributes, and the comment is a case-sensitive check.
     */
    @Override
    public boolean equals(Object compareTo) {
        boolean isHiveColumnInfo = compareTo instanceof HiveColumnInfo;

        return  isHiveColumnInfo &&
                this.columnPosition == ((HiveColumnInfo)compareTo).columnPosition &&
                this.name.equalsIgnoreCase(((HiveColumnInfo)compareTo).name) &&
                this.dataType.equalsIgnoreCase(((HiveColumnInfo)compareTo).dataType) &&
                this.comment.equals(((HiveColumnInfo)compareTo).comment);
    }
}
