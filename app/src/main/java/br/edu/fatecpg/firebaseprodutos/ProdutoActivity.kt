package br.edu.fatecpg.firebaseprodutos

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.fatecpg.firebaseprodutos.databinding.ActivityProdutoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProdutoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProdutoBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProdutoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSalvar.setOnClickListener {
            val nome = binding.etNome.editText?.text.toString().trim()
            val preco = binding.etPreco.editText?.text.toString().trim()
            val descricao = binding.etDescricao.editText?.text.toString().trim()

            if (nome.isEmpty() || preco.isEmpty() || descricao.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val uid = auth.currentUser?.uid ?: return@setOnClickListener

            val produto = hashMapOf(
                "nome" to nome,
                "preco" to preco,
                "descricao" to descricao,
                "uid" to uid
            )

            firestore.collection("produtos")
                .add(produto)
                .addOnSuccessListener {
                    Toast.makeText(this, "Produto salvo!", Toast.LENGTH_SHORT).show()
                    binding.tvNomeProduto.text = nome
                    binding.tvPrecoProduto.text = "R$ $preco"
                    binding.tvDescricaoProduto.text = descricao
                    binding.cardProduto.visibility = View.VISIBLE
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}