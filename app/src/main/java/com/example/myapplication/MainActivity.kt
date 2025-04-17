package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding

// 추가 다이얼로그,
class MainActivity : AppCompatActivity(), AddFragment.OnAddItemListener,
    DetailDialogFragment.OnItemEditedListener {

    lateinit var binding: ActivityMainBinding
    lateinit var adapter: MyAdapter
    var datas: MutableList<Pair<String, Boolean>> = mutableListOf()
    val fileName = "memo.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        datas = loadDataFromFile()
        adapter = MyAdapter(
            datas,
            onDeleteClick = { deletedItem ->
                datas.remove(deletedItem)
                adapter.notifyDataSetChanged()
                saveDataToFile(datas)
            },
            onItemClick = { clickedItem ->
                DetailDialogFragment.newInstance(clickedItem.first)
                    .show(supportFragmentManager, "DetailDialog")
            },
            onCheckChanged = {
                saveDataToFile(datas)  // 체크 상태 변경 시 바로 저장
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        binding.mainFab.setOnClickListener {
            val addFragment = AddFragment()
            // AddFragment를 프래그먼트 컨테이너에 추가
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, addFragment) // fragment_container는 프래그먼트가 들어갈 컨테이너 ID
                .addToBackStack(null) // 뒤로가기 버튼을 통해 돌아올 수 있도록
                .commit()
        }
    }

    override fun onItemEdited(original: String, updated: String) {
        val index = datas.indexOfFirst { it.first == original }
        if (index != -1) {
            datas[index] = Pair(updated, datas[index].second)
            adapter.notifyItemChanged(index)
            saveDataToFile(datas)
        }
    }

    override fun onItemAdd(item: String) {
        datas.add(Pair(item, false))
        adapter.notifyDataSetChanged()
        saveDataToFile(datas)
    }

    // 내용 저장하기 (앱에서 새로운 항목 추가될 때 호출)
    fun saveDataToFile(dataList: List<Pair<String, Boolean>>) {
        val fileOutput = openFileOutput(fileName, MODE_PRIVATE)
        fileOutput.bufferedWriter().use { writer ->
            dataList.forEach {
                // "할일||체크여부" 형식으로 저장
                writer.write("${it.first}||${it.second}")
                writer.newLine()
            }
        }
    }

    // 내용 불러오기 (앱 시작 시)
    fun loadDataFromFile(): MutableList<Pair<String, Boolean>> {
        return try {
            val fileInput = openFileInput(fileName)
            fileInput.bufferedReader().readLines().map { line ->
                val parts = line.split("||")
                if (parts.size == 2) {
                    Pair(parts[0], parts[1].toBoolean())
                } else {
                    Pair(line, false)
                }
            }.toMutableList()
        } catch (e: Exception) {
            mutableListOf()
        }
    }

}