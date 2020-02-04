package service.listener

import javax.jms.Message
import javax.jms.MessageListener
import javax.jms.TextMessage

class ProcessMessageDefault : MessageListener{

    override fun onMessage(message: Message?) {
        val t = message as TextMessage
        println(t.text)
    }
}