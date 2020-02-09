package com.github.marcelop3251.queuesmanager.service.impl

import javax.jms.*

open class BaseConfigQueue {

    internal fun createConsumer(
        connectionFactory: ConnectionFactory,
        nameQueue: String
    ): MessageConsumer = connectionFactory.createConnection().let {
        it.start()
        val session = createSession(it)
        session.createConsumer(createQueue(session, nameQueue))
    }

    internal fun createProducer(
        connectionFactory: ConnectionFactory,
        nameQueue: String
    ): MessageProducer = connectionFactory.createConnection().let {
        val session = createSession(it)
        session.createProducer(createQueue(session, nameQueue))
    }

    private fun createQueue(session: Session, nameQueue: String) = session.createQueue(nameQueue)

    private fun createSession(it: Connection) = it.createSession(false, Session.AUTO_ACKNOWLEDGE)


}
