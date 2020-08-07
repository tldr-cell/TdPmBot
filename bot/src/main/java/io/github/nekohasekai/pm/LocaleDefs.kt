package io.github.nekohasekai.pm

import io.github.nekohasekai.nekolib.i18n.LocaleController


private val LocaleController.pm by LocaleController.receiveLocaleSet("pm")
private val string = LocaleController.receiveLocaleString { pm }

internal val LocaleController.HELP_MSG by string
internal val LocaleController.PRIVATE_INSTANCE by string

internal val LocaleController.CREATE_BOT_DEF by string
internal val LocaleController.INPUT_BOT_TOKEN by string
internal val LocaleController.INVALID_BOT_TOKEN by string
internal val LocaleController.FETCHING_INFO by string
internal val LocaleController.CREATING_BOT by string
internal val LocaleController.ALREADY_EXISTS by string
internal val LocaleController.FINISH_CREATION by string
internal val LocaleController.CREATE_FINISHED by string

internal val LocaleController.DELETE_BOT_DEF by string
internal val LocaleController.NO_BOTS by string
internal val LocaleController.SELECT_TO_DELETE by string
internal val LocaleController.INVALID_SELECTED by string
internal val LocaleController.DELETE_CONFIRM by string
internal val LocaleController.DELETE_CONFIRM_REGEX by string
internal val LocaleController.CONFIRM_NOT_MATCH by string
internal val LocaleController.STOPPING by string
internal val LocaleController.DELETING by string
internal val LocaleController.BOT_DELETED by string
internal val LocaleController.BOT_AUTH_FAILED by string
internal val LocaleController.BOT_LOGOUT by string

internal val LocaleController.SET_STARTS_DEF by string
internal val LocaleController.SELECT_TO_SET by string
internal val LocaleController.JUMP_TO_SET by string

internal val LocaleController.INPUT_MESSAGES by string
internal val LocaleController.MESSAGE_ADDED by string
internal val LocaleController.MESSAGE_ADDED_FWD by string

internal val LocaleController.INPUT_NOTICE by string
internal val LocaleController.BANDED_BY by string
internal val LocaleController.JOINED_NOTICE by string
internal val LocaleController.JOIN_NON_PM by string
internal val LocaleController.EXITED by string
internal val LocaleController.SENT by string
internal val LocaleController.REPLIED by string
internal val LocaleController.EDITED by string
internal val LocaleController.DELETED by string
internal val LocaleController.RECORD_NF by string
internal val LocaleController.REPLIED_NF by string
internal val LocaleController.MESSAGE_DELETED by string
internal val LocaleController.MESSAGE_EDITED by string

internal val LocaleController.PM_HELP by string