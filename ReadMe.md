# DemoMeow(Demo喵喵)
## Description
 Watch cat on the Android phone.
 The cat pictures come from  [TheCatAPI](https://thecatapi.com/) (it's easy to sign up and get the token.\)
 This app based on [this Medium Article](https://medium.com/@gunayadem.dev/boost-your-android-apps-with-koin-and-coroutines-using-mvvm-in-kotlin-d30fe436ab4c) written by [Adem Gunay](https://github.com/ademgunay/)
 
 觀看貓圖在你的Android手機上。
 App的貓圖都是從[TheCatAPI](https://thecatapi.com/)取得的。
 此App是基於AdemGunay撰寫的Medium文章去進行擴展。

## Functionality
1. Keep your fravorite cat image's urls, not image self, 
   but this app use Glide library that will automatelly cache your picutres (I use the Room Database, just SQLite) 
2. Get cat's pictures randomly from TheCatApi(allways 25)
3. Zoom in your cute kitty picture
-------
   1. 儲存你喜歡的貓咪圖的網址，非圖片本身， 
   但app使用Glide函式庫，它會自動快取你的圖片本身 (我使用Room Datbase, 只是SQLite) 
2. 隨機從TheCatApi取得圖片(總是25張)
3. 放大觀看你可愛的喵貓圖
## Requirement

### 1. Git clone my project and don't forget to insert TheCatApi's key to the file "local.properties"。
```bash
$ git clone 
```
```c
catApiKey="<--Your theCatApi's token-->"
```
### 2. Maybe you will rebuild and  and "invalidate restart" in your Android Studio.
---
### 1. 使用Git克隆我的專案，以及別忘記將TheCatApi的Key放到Android專案的檔案"local.properties"
```bash
$ git clone 
```
```c
catApiKey="<--Your theCatApi's token-->"
```
### 2.可能你會需要rebuild你的專案，保險點你需要"invalidate restart"你的Android Studio。