package org.techtown.guru2project

data class Todo (val todo: String? = null, //해야 할 일의 이름
                 var date: String? = null, //사용자가 설정한 날짜
                 var place: String? = null, //사용자가 설정한 위치의 이름
                 var latitude: String? = null,  //place의 위도
                 var longitude: String? = null, //place의 경도
                 var address: String? = null,   //place의 간략한 주소
                 var index: String? = null,     //index 컬러
                 var isDone: Boolean? = null)   //완료 여부
//isDone이 true인 경우 완료한 일
