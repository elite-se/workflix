package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.ProcessTemplateDAOInterface
import de.se.team3.logic.domain.ProcessTemplate
import de.se.team3.logic.domain.User
import de.se.team3.persistence.meta.ProcessTemplates
import de.se.team3.persistence.meta.Users
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.innerJoin
import me.liuwj.ktorm.dsl.limit
import me.liuwj.ktorm.dsl.select

object ProcessTemplateDAO: ProcessTemplateDAOInterface {

    override fun getAllProcessTemplates(offset: Int, limit: Int): Pair<List<ProcessTemplate>, Int> {
        val processTemplates = ArrayList<ProcessTemplate>()
        val result = ProcessTemplates
            .innerJoin(Users, on = Users.ID eq ProcessTemplates.ownerID)
            .select()
            .limit(offset, limit)

        for (row in result) {
            val owner = User(row[Users.ID]!!, row[Users.name]!!, row[Users.displayname]!!, row[Users.email]!!)
            val template = ProcessTemplate(row[ProcessTemplates.ID]!!, row[ProcessTemplates.title]!!, row[ProcessTemplates.durationLimit]!!, owner)
            processTemplates.add(template)
        }

        return Pair(processTemplates.toList(), 0)
    }


}