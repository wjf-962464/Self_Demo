package com.wjf.self_demo.test.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/** @author wangjiafeng */
// @JsonAdapter(value = StudentAdapter.class)
public class Student {
    //    @SerializedName("code")
    int a;
    String name;

    @Override
    public String toString() {
        return "Student{" + "code=" + a + ", name='" + name + '\'' + '}';
    }
}
/** @author wangjiafeng */
class StudentAdapter extends TypeAdapter<Student> {

    @Override
    public void write(JsonWriter out, Student value) throws IOException {}

    @Override
    public Student read(JsonReader in) throws IOException {
        Student student = new Student();
        in.beginObject();
        while (in.hasNext()) {

            switch (in.nextName()) {
                case "code":
                    student.a = in.nextInt() + 1;
                    break;
                case "name":
                    student.name = in.nextString() + "lueluelue";
                    break;
                default:
                    break;
            }
        }
        in.endObject();
        return student;
    }
}
