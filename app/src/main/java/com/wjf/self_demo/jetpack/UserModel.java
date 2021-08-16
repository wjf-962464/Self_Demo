package com.wjf.self_demo.jetpack;

import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @author WJF
 */
public class UserModel extends ViewModel {
    private MutableLiveData<String> name;
    private MutableLiveData<Integer> age;

    public UserModel(String name,int age) {
        this.name=new MutableLiveData<>(name);
        this.age=new MutableLiveData<>(age);
    }

    @Bindable
    public MutableLiveData<String> getName() {
        return name;
    }

    public MutableLiveData<Integer> getAge() {
        return age;
    }

    public void setName(String name) {
        this.name.setValue(name);

    }

    public void setAge(int age) {
        this.age.setValue(age);

    }
}
