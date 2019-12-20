package de.se.team3.logic.container

import de.se.team3.logic.domain.ProcessGroup
import de.se.team3.logic.domain.ProcessTemplate
import de.se.team3.webservice.containerInterfaces.ProcessGroupContainerInterface

object ProcessGroupContainer : ProcessGroupContainerInterface {
    override fun getAllProcessGroups(page: Int): Pair<List<ProcessGroup>, Int> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getProcessGroup(processGroupId: Int): ProcessTemplate {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun createProcessGroup(processGroup: ProcessGroup): Int {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteProcessGroup(processGroupId: Int) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
