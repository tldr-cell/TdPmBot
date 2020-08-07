package io.github.nekohasekai.pm.instance

import cn.hutool.core.date.SystemClock
import io.github.nekohasekai.nekolib.core.client.TdException
import io.github.nekohasekai.nekolib.core.client.TdHandler
import io.github.nekohasekai.nekolib.core.raw.getChat
import io.github.nekohasekai.nekolib.core.raw.getMessage
import io.github.nekohasekai.nekolib.core.utils.*
import io.github.nekohasekai.nekolib.i18n.failed
import io.github.nekohasekai.pm.*
import io.github.nekohasekai.pm.database.MessageRecords
import io.github.nekohasekai.pm.database.PmInstance
import td.TdApi

class OutputHandler(private val admin: Int, pmInstance: PmInstance) : TdHandler(), PmInstance by pmInstance {

    var currentChat = 0L

    override suspend fun onNewMessage(userId: Int, chatId: Long, message: TdApi.Message) {

        if (chatId != userId.toLong() || userId != admin) return

        fun saveSent(targetChat: Long, sentMessageId: Long) {

            database {

                messages.new(message.id) {

                    type = MessageRecords.MESSAGE_TYPE_OUTPUT_MESSAGE

                    this.chatId = targetChat

                    targetId = sentMessageId

                    createAt = (SystemClock.now() / 100L).toInt()

                }

                messages.new(sentMessageId) {

                    type = MessageRecords.MESSAGE_TYPE_OUTPUT_FORWARDED

                    this.chatId = targetChat

                    targetId = message.id

                    createAt = (SystemClock.now() / 100L).toInt()

                }

            }

        }

        if (message.replyToMessageId == 0L) {

            if (currentChat == 0L) {

                if (sudo is PmBot) {

                    sudo make L.PM_HELP sendTo chatId

                }

                return

            }

            val sentMessage = try {

                sudo make message.asInputOrForward syncTo currentChat

            } catch (e: TdException) {

                sudo make L.failed { e.message } replyTo message send deleteDelay(message)

                return

            }

            saveSent(currentChat, sentMessage.id)

            sudo make L.SENT replyTo message send deleteDelay()

            return

        }

        val record = database {

            messages.find { messageRecords.messageId eq message.replyToMessageId }.firstOrNull()

        }

        if (record == null) {

            sudo make L.RECORD_NF replyTo message send deleteDelay(message)

            return

        }

        suspend fun getTargetChat() = try {

            getChat(record.chatId)

        } catch (e: TdException) {

            sudo make L.failed { BANDED_BY } replyTo message send deleteDelay(message)

            null

        }

        when (record.type) {

            MessageRecords.MESSAGE_TYPE_INPUT_MESSAGE -> {

                defaultLog.warn("Please delete the data after changing the bot admin.")

            }

            MessageRecords.MESSAGE_TYPE_INPUT_NOTICE -> {

                val targetUser = getTargetChat() ?: return

                val sentMessage = try {

                    sudo make message.content.asInput!! syncTo targetUser.id

                } catch (e: TdException) {

                    sudo make L.failed { e.message } replyTo message send deleteDelay(message)

                    return

                }

                saveSent(targetUser.id, sentMessage.id)

                sudo make L.SENT replyTo message send deleteDelay()

            }

            MessageRecords.MESSAGE_TYPE_INPUT_FORWARDED,
            MessageRecords.MESSAGE_TYPE_OUTPUT_FORWARDED -> {

                val targetUser = getTargetChat() ?: return

                val targetMessage = try {

                    getMessage(targetUser.id, record.targetId!!)

                } catch (e: TdException) {

                    sudo make L.failed { REPLIED_NF } replyTo message send deleteDelay(message)

                    return

                }

                val sendedMessage = try {

                    sudo make message.content.asInput!! syncReplyTo targetMessage

                } catch (e: TdException) {

                    sudo make L.failed { e.message } replyTo message send deleteDelay(message)

                    return

                }

                saveSent(targetUser.id, sendedMessage.id)

                sudo make L.REPLIED replyTo message send deleteDelay()

            }

        }

    }

}
