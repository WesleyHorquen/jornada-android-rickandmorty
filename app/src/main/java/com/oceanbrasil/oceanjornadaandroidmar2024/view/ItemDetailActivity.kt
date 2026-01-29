package com.oceanbrasil.oceanjornadaandroidmar2024.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.oceanbrasil.oceanjornadaandroidmar2024.R
import com.oceanbrasil.oceanjornadaandroidmar2024.model.api.ApiService
import com.oceanbrasil.oceanjornadaandroidmar2024.model.domain.Item
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ItemDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        val id = intent.getIntExtra("ID", 0)

        if (id == 0) {
            finish()
            return
        }

        Toast.makeText(this, "Carregar API para o ID $id", Toast.LENGTH_LONG).show()

        // Apontamos o Retrofit para a nova API
        val retrofit = Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        // A chamada agora busca por um único `Item`
        apiService.carregarItem(id).enqueue(object : Callback<Item> {
            override fun onResponse(call: Call<Item>, response: Response<Item>) {
                response.body()?.let {
                    Log.d("API", it.toString())

                    val tvNome = findViewById<TextView>(R.id.tvNome)
                    val ivImagem = findViewById<ImageView>(R.id.ivImagem)

                    tvNome.text = it.nome
                    Glide.with(ivImagem).load(it.imagem).into(ivImagem)
                }
            }

            override fun onFailure(call: Call<Item>, t: Throwable) {
                Log.e("API", "Erro ao carregar dados da API.", t)
            }
        })
    }
}
