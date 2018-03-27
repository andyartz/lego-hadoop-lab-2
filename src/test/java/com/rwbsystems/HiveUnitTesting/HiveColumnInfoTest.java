package com.rwbsystems.HiveUnitTesting;

import org.junit.Test;

import static org.junit.Assert.*;

public class HiveColumnInfoTest {
    @Test
    public void equals_ExactlySame_True() {
        HiveColumnInfo sut = new HiveColumnInfo(0, "col1", "int", "my comment");

        assertTrue(sut.equals(new HiveColumnInfo(0, "col1", "int", "my comment")));
    }

    @Test
    public void equals_MixedCaseColumnName_True() {
        HiveColumnInfo sut = new HiveColumnInfo(0, "col1", "int", "my comment");

        assertTrue(sut.equals(new HiveColumnInfo(0, "COl1", "int", "my comment")));
    }

    @Test
    public void equals_MixedCaseDatatype_True() {
        HiveColumnInfo sut = new HiveColumnInfo(0, "col1", "int", "my comment");

        assertTrue(sut.equals(new HiveColumnInfo(0, "col1", "InT", "my comment")));
    }

    @Test
    public void equals_MixedCaseComment_False() {
        HiveColumnInfo sut = new HiveColumnInfo(0, "col1", "int", "my comment");

        assertFalse(sut.equals(new HiveColumnInfo(0, "col1", "int", "My comment")));
    }

    @Test
    public void equals_DifferentColumnPosition_False() {
        HiveColumnInfo sut = new HiveColumnInfo(0, "col1", "int", "my comment");

        assertFalse(sut.equals(new HiveColumnInfo(1, "col1", "int", "my comment")));
    }

    @Test
    public void equals_DifferentColumnName_False() {
        HiveColumnInfo sut = new HiveColumnInfo(0, "col1", "int", "my comment");

        assertFalse(sut.equals(new HiveColumnInfo(0, "col2", "int", "my comment")));
    }

    @Test
    public void equals_DifferentDatatype_False() {
        HiveColumnInfo sut = new HiveColumnInfo(0, "col1", "int", "my comment");

        assertFalse(sut.equals(new HiveColumnInfo(0, "col1", "timestamp", "my comment")));
    }

    @Test
    public void equals_DifferentComment_False() {
        HiveColumnInfo sut = new HiveColumnInfo(0, "col1", "int", "my comment");

        assertFalse(sut.equals(new HiveColumnInfo(0, "col1", "int", "my commen")));
    }

}