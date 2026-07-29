package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.AttendanceRecord
import com.example.data.model.AuditLog
import com.example.data.model.CampaignEvent
import com.example.data.model.Collaborator
import com.example.data.model.GoalRecord
import com.example.data.model.Leadership
import com.example.data.model.MaterialDelivery
import com.example.data.model.PaymentRecord

@Database(
    entities = [
        Collaborator::class,
        MaterialDelivery::class,
        PaymentRecord::class,
        AttendanceRecord::class,
        GoalRecord::class,
        Leadership::class,
        CampaignEvent::class,
        AuditLog::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CampaignDatabase : RoomDatabase() {
    abstract fun collaboratorDao(): CollaboratorDao
    abstract fun materialDao(): MaterialDao
    abstract fun paymentDao(): PaymentDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun goalDao(): GoalDao
    abstract fun leadershipDao(): LeadershipDao
    abstract fun eventDao(): EventDao
    abstract fun auditLogDao(): AuditLogDao

    companion object {
        @Volatile
        private var INSTANCE: CampaignDatabase? = null

        fun getDatabase(context: Context): CampaignDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CampaignDatabase::class.java,
                    "campaign_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
