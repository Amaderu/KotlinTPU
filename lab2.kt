/**
 Задание
Напишите программу, которая имитирует работу телефонной станции. В программе должно
быть два класса:
• Abonent – содержит поля с именем абонента и его номером, а также список входящих
и исходящих звонков
• Station – содержит список абонентов и два метода:
o call(from: String, to: String) – ищет абонентов с именами from и to, и если
они найдены, то первому добавляет в журнал вызовов строку «Исходящий к
<…>», где вместо <…> подставляется имя абонента to, а второму добавляет в
журнал вызовов строку «Входящий от <…>», где вместо <…> подставляется имя
абонента from.
o showStat() – выводит полные журналы звонков каждого абонента в формате:
Журнал звонков абонента <…>:
 Входящий от <…>
 Входящий от <…>
 Исходящий от <…>
Журнал звонков абонента <…>:
 Исходящий от <…>
 Исходящий от <…>
В функции main следует создать объект Station, добавить в него несколько абонентов,
несколько раз вызвать функцию call() для имитации звонков между разными абонентами, а
затем вызвать функцию showStat() для отображения журнала звонков.
 */
import java.util.UUID;
import java.util.regex.Pattern;

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.TimeZone

import kotlinx.datetime.format.*


import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds




enum class CallTypes{
    INCOMING, OUTGOING
}

data class Call(var name : String, val number : String, val callType: CallTypes = CallTypes.OUTGOING){
}

class Abonent(){
    private val id: UUID
    	fun getId():UUID{return id}
    private var name: String = ""
	fun getName():String {return name}
    private var phoneNumber: String = ""
    fun getPhoneNumber():String {return phoneNumber}
    
    var calls: MutableList<Call> = mutableListOf()
    	get() = field

    init{
        id = UUID.randomUUID()
    }
    
    constructor(Name: String, PhoneNumber: String) : this(){
        name = Name
        phoneNumber = PhoneNumber
    }
}


class Station() {
    var abonents: MutableList<Abonent>
    fun call(from: String, to: String){
        val contacts = abonents.filter{abonent -> abonent.getName() in listOf(from, to)}
        //FIXME filter may find many contacts by @from@ number
        val canCall = if ((contacts?.count() ?: 0) >= 2) true else false
        
        if(canCall){
            val fromCall = contacts.find{it.getName() == from}
            val toCall = contacts.find{it.getName() == to}
            
            
            //FIXME preparing to chose correct contact
            	fromCall?.calls?.add(
                    Call(
                        toCall!!.getName(), 
                        toCall!!.getPhoneNumber(), 
                        CallTypes.OUTGOING
                    )
                )
                
            	toCall?.calls?.add(
            		Call(
                        fromCall!!.getName(),
                        fromCall!!.getPhoneNumber(),
                        CallTypes.INCOMING
                    )
            	)
            
        }
		
    }
        
    fun print(msg: String) = println(msg)
    
    fun showStat(){
        for(abonent: Abonent in abonents){
            println("""Журнал звонков абонента <${abonent!!.getName()}>:""")
            for(call: Call in abonent.calls){
                when(call.callType){
                    CallTypes.INCOMING -> this.print(""" Входящий от <${call!!.name}>""")
                    CallTypes.OUTGOING -> this.print(""" Исходящий к <${call!!.name}>""")
                    else -> {
                        throw IllegalStateException()
                    }
            
            	}
            }
        }
    }
    
    init{
        abonents = mutableListOf()
    }
    
    constructor(Abonents: MutableList<Abonent>) : this(){
       abonents = Abonents
    }
    
} 

// Журнал звонков абонента <…>:
//  Входящий от <…>
//  Входящий от <…>
//  Исходящий от <…>
// Журнал звонков абонента <…>:
//  Исходящий от <…>
//  Исходящий от <…>



fun main() {
// 	Create Abonents
    val ivan = Abonent("ivan","+7-33-22-00")
    val maria = Abonent("maria","+7-25-54-40")
    val evgenyi = Abonent("evgenyi","+7-13-45-61")
    
//	Create station
    val stationOne = Station(mutableListOf(ivan, maria, evgenyi))
    stationOne.call(ivan.getName(), maria.getName())
    stationOne.call(evgenyi.getName(), maria.getName())
    stationOne.call(maria.getName(), ivan.getName())
    
    stationOne.showStat()
}