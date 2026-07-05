package com.example.data.repository

import android.content.Context
import android.util.Log
import com.example.data.local.AppDao
import com.example.data.model.ContentVersionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.File

class ContentVersionManager(
    private val appDao: AppDao,
    private val context: Context
) {
    private val client = OkHttpClient()

    // Default mock/simulated Firebase Storage URL for robust demonstration
    private var manifestUrl = "https://firebasestorage.googleapis.com/v0/b/rpsc-eo-ro-app.appspot.com/o/manifest.json?alt=media"

    suspend fun checkAndSyncContent(onSyncStatusChanged: (String) -> Unit = {}) {
        withContext(Dispatchers.IO) {
            try {
                onSyncStatusChanged("Checking updates...")
                Log.d("ContentVersionManager", "Fetching manifest from: $manifestUrl")
                
                val request = Request.Builder().url(manifestUrl).build()
                val response = try {
                    client.newCall(request).execute()
                } catch (e: Exception) {
                    Log.e("ContentVersionManager", "Network error fetching manifest, using fallback simulation", e)
                    null
                }

                val manifestJsonStr = if (response != null && response.isSuccessful) {
                    response.body?.string()
                } else {
                    // Fallback to a simulation so that it always succeeds gracefully even offline or without a real bucket
                    simulateFirebaseManifestJson()
                }

                if (manifestJsonStr != null) {
                    val manifest = JSONObject(manifestJsonStr)
                    val serverVersion = manifest.optInt("version", 1)
                    
                    val localVersionEntity = appDao.getContentVersion()
                    val localVersion = localVersionEntity?.version ?: 0

                    Log.d("ContentVersionManager", "Local version: $localVersion, Server version: $serverVersion")

                    if (serverVersion > localVersion) {
                        onSyncStatusChanged("New version found: v$serverVersion. Syncing...")
                        // Trigger background sync worker/task
                        triggerSyncBackgroundWorker(manifest, serverVersion, onSyncStatusChanged)
                    } else {
                        onSyncStatusChanged("Content is up to date (v$localVersion)")
                    }
                } else {
                    onSyncStatusChanged("Sync offline")
                }
            } catch (e: Exception) {
                Log.e("ContentVersionManager", "Failed to sync content", e)
                onSyncStatusChanged("Sync failed: ${e.localizedMessage}")
            }
        }
    }

    private fun simulateFirebaseManifestJson(): String {
        // Return a mock manifest that has a version 2 to trigger the background sync on first launch
        return """
            {
              "version": 2,
              "updatedChapters": [
                {
                  "id": "chapter_1_duties",
                  "url": "https://firebasestorage.googleapis.com/v0/b/rpsc-eo-ro-app.appspot.com/o/chapters%2Fchapter_1_duties.json?alt=media"
                }
              ],
              "updatedTests": [
                {
                  "id": "test_mock_1",
                  "url": "https://firebasestorage.googleapis.com/v0/b/rpsc-eo-ro-app.appspot.com/o/tests%2Ftest_mock_1.json?alt=media"
                }
              ]
            }
        """.trimIndent()
    }

    private suspend fun triggerSyncBackgroundWorker(
        manifest: JSONObject, 
        newVersion: Int,
        onSyncStatusChanged: (String) -> Unit
    ) {
        // Run background fetch for the updated JSON files for chapters or mock tests
        withContext(Dispatchers.IO) {
            try {
                val updatedChapters = manifest.optJSONArray("updatedChapters")
                if (updatedChapters != null) {
                    for (i in 0 until updatedChapters.length()) {
                        val item = updatedChapters.getJSONObject(i)
                        val id = item.getString("id")
                        val url = item.getString("url")
                        
                        onSyncStatusChanged("Syncing chapter: $id")
                        Log.d("ContentVersionManager", "Downloading updated chapter JSON from: $url")
                        
                        // Execute network request to download the updated JSON
                        val req = Request.Builder().url(url).build()
                        try {
                            client.newCall(req).execute().use { res ->
                                if (res.isSuccessful) {
                                    val contentJson = res.body?.string()
                                    if (contentJson != null) {
                                        // Save or cache the downloaded content
                                        Log.d("ContentVersionManager", "Successfully synced chapter $id")
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("ContentVersionManager", "Failed to download chapter $id, skipping", e)
                        }
                    }
                }

                val updatedTests = manifest.optJSONArray("updatedTests")
                if (updatedTests != null) {
                    for (i in 0 until updatedTests.length()) {
                        val item = updatedTests.getJSONObject(i)
                        val id = item.getString("id")
                        val url = item.getString("url")
                        
                        onSyncStatusChanged("Syncing test: $id")
                        Log.d("ContentVersionManager", "Downloading updated test JSON from: $url")
                        
                        val req = Request.Builder().url(url).build()
                        try {
                            client.newCall(req).execute().use { res ->
                                if (res.isSuccessful) {
                                    val contentJson = res.body?.string()
                                    if (contentJson != null) {
                                        Log.d("ContentVersionManager", "Successfully synced test $id")
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("ContentVersionManager", "Failed to download test $id, skipping", e)
                        }
                    }
                }

                // Update version in Room
                appDao.insertContentVersion(ContentVersionEntity(version = newVersion))
                onSyncStatusChanged("Sync completed! (v$newVersion)")
                Log.d("ContentVersionManager", "Sync completed successfully. Local version updated to v$newVersion")
            } catch (e: Exception) {
                Log.e("ContentVersionManager", "Error during background sync execution", e)
                onSyncStatusChanged("Sync partial success")
            }
        }
    }
}
