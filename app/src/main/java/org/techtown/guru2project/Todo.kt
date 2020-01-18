package org.techtown.guru2project

data class Todo (val todo: String, //해야 할 일의 이름
                 var date: String, //사용자가 설정한 날짜
                 var place: String, //사용자가 설정한 위치의 이름
                 var latitude: String,  //place의 위도
                 var longitude: String, //place의 경도
                 var address: String,   //place의 간략한 주소
                 var index: String,     //index 컬러
                 var isDone: Boolean)   //완료 여부
//isDone이 true인 경우 완료한 일
