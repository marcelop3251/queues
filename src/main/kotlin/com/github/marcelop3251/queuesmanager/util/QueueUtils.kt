package com.github.marcelop3251.queuesmanager.util

import javax.jms.Connection
import javax.jms.ConnectionFactory
import javax.jms.Session

object QueueUtils {

    fun createDestination(session: Session, nameQueue: String) = session.createQueue(nameQueue)

    fun createSession(connection: Connection) =
        connection.createSession(false, Session.AUTO_ACKNOWLEDGE)

    fun createConnection(connectionFactory: ConnectionFactory): Connection = connectionFactory.createConnection()
}