package com.example.flo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.flo.data.entities.User

@Dao
interface UserDao {

    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM UserTable")
    fun getUsers():List<User>

    // userTable에서 입력받은 email과 password가 똑같은 사람을 모두 가져오라는 구문
    @Query("SELECT * FROM UserTable WHERE email=:email AND password=:password")
    fun getUser(email:String, password:String): User?

    @Query("SELECT * FROM UserTable WHERE id=:id")
    fun getUserName(id:Int): User?

}