package com.anufriev.data.repository

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CallLog
import android.provider.ContactsContract
import com.anufriev.data.entity.Contact
import com.anufriev.utils.ext.getShortPhone
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ContactRepository(private val context: Context) {

    suspend fun getAllContacts(): List<Contact> = withContext(Dispatchers.IO) {
        context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )?.use {
            getContactsFromCursor(it)
        }.orEmpty()
    }

    private fun getContactsFromCursor(cursor: Cursor): List<Contact> {
        if (cursor.moveToFirst().not()) return emptyList()
        val list = mutableListOf<Contact>()
        do {
            val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
            val name = cursor.getString(nameIndex).orEmpty()

            val idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
            val id = cursor.getLong(idIndex)

            list.add(Contact(id = id, name = name, phones = getPhonesForContact(id)))
        } while (cursor.moveToNext())

        return list
    }

    private fun getPhonesForContact(contactId: Long): List<String> {
        return context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
            arrayOf(contactId.toString()),
            null
        )?.use {
            getPhonesFromCursor(it)
        }.orEmpty()
    }

    private fun getPhonesFromCursor(cursor: Cursor): List<String> {
        if (cursor.moveToFirst().not()) return emptyList()
        val list = mutableListOf<String>()
        do {
            val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val number = cursor.getString(numberIndex)
            list.add(getShortPhone(number))
        } while (cursor.moveToNext())

        return list
    }

    fun deleteContactById(id: Long) {
        val cr = context.contentResolver
        val cur = cr.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )
        cur?.let {
            try {
                if (it.moveToFirst()) {
                    do {
                        if (cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup._ID)) == id.toString()) {
                            val lookupKey =
                                cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY))
                            val uri = Uri.withAppendedPath(
                                ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                                lookupKey
                            )
                            cr.delete(uri, null, null)
                            break
                        }

                    } while (it.moveToNext())
                }

            } catch (e: Exception) {
                println(e.stackTrace)
            } finally {
                it.close()
            }
        }
    }

    suspend fun getAllLogCall(): List<Contact> = withContext(Dispatchers.IO) {
        context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            arrayOf(
                CallLog.Calls._ID,
                CallLog.Calls.DATE,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DURATION,
                CallLog.Calls.TYPE
            ),
            null,
            null,
            null
        )?.use {
            getCallLog(it)
        }.orEmpty()
    }

    private fun getCallLog(cursor: Cursor): List<Contact> {
        if (cursor.moveToFirst().not()) return emptyList()
        val list = mutableListOf<Contact>()
        do {
            val id = cursor.getLong(cursor.getColumnIndex(CallLog.Calls._ID))

            val indexNumber = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            val number = getShortPhone(cursor.getString(indexNumber))

            list.add(Contact(id = id, name = "", phones = listOf(number)))
        } while (cursor.moveToNext())

        return list
    }

    fun deleteCallNumberById(id: Long) {
        context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null, null, null, null
        )?.let { cur ->
            try {
                if (cur.moveToFirst()) {
                    do {
                        if (cur.getString(cur.getColumnIndex(CallLog.Calls._ID)) == id.toString()) {
                            val number = cur.getString(cur.getColumnIndex(CallLog.Calls.NUMBER))
                            val uri = Uri.parse(CallLog.Calls.CONTENT_URI.toString())
                            context.contentResolver.delete(
                                uri,
                                "${CallLog.Calls.NUMBER}=$number",
                                null
                            )
                            break
                        }

                    } while (cur.moveToNext())
                }

            } catch (e: Exception) {
                println(e.stackTrace)
            } finally {
                cur.close()
            }
        }
    }
}