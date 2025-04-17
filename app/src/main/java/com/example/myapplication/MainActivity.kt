package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
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
        Log.d("MainActivity", "onCreate called") // 이 로그가 출력되는지 확인

        datas = loadDataFromFile()
        // 로드된 데이터 확인 (Logcat 확인)
        Log.d("MainActivity", "Loaded data for RecyclerView: $datas")

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
        Log.d("MainActivity", "Before setting adapter")
        binding.recyclerView.adapter = adapter
        Log.d("MainActivity", "After setting adapter")

        // RecyclerView 보이기
        binding.recyclerView.visibility = View.VISIBLE

        binding.mainFab.setOnClickListener {
            val addFragment = AddFragment()
            // RecyclerView 숨기기
            binding.recyclerView.visibility = View.GONE
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
        // 데이터 추가
        datas.add(Pair(item, false))
        // 어댑터에 변경 사항을 알림
        adapter.notifyDataSetChanged() // 새로 추가된 항목만 갱신
        // 파일에 저장
        saveDataToFile(datas)
        // AddFragment가 끝나면 RecyclerView를 다시 보이게 하고 Fragment를 제거
        binding.recyclerView.visibility = View.VISIBLE
        supportFragmentManager.popBackStack() // Fragment를 pop하여 MainActivity로 돌아옴
    }

    // 내용 저장하기 (앱에서 새로운 항목 추가될 때 호출)
    fun saveDataToFile(dataList: List<Pair<String, Boolean>>) {
        try {
            val fileOutput = openFileOutput(fileName, MODE_APPEND)
            fileOutput.bufferedWriter().use { writer ->
                dataList.forEach {
                    // 파일에 저장할 때 로그 출력
                    Log.d("MainActivity", "Saving data: ${it.first} || ${it.second}")

                    // "할일||체크여부" 형식으로 저장
                    writer.write("${it.first}||${it.second}")
                    writer.newLine()
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error saving data", e)
        }
    }

    // 내용 불러오기 (앱 시작 시)
    fun loadDataFromFile(): MutableList<Pair<String, Boolean>> {
        return try {
            val fileInput = openFileInput(fileName)
            val lines = fileInput.bufferedReader().readLines()

            if (lines.isEmpty()) {
                Log.d("MainActivity", "File is empty.")
            } else {
                Log.d("MainActivity", "Loaded data: $lines")
            }

            // 로드된 데이터를 로그로 확인
            Log.d("MainActivity", "Loaded data: $lines")

            lines.map { line ->
                val parts = line.split("||")
                if (parts.size == 2) {
                    Pair(parts[0], parts[1].toBoolean())
                } else {
                    Pair(line, false)
                }
            }.toMutableList()
        } catch (e: Exception) {
            // 오류가 발생하면 빈 리스트 반환
            Log.e("MainActivity", "Error loading data", e)
            mutableListOf()
        }
    }

}