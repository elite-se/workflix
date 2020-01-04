package de.se.team3.logic.domain.processTemplateUtil

import de.se.team3.logic.domain.TaskTemplate

object ProcessTemplateCycleDetection {

    enum class ProcessingStatus {
        NotVisited, InVisitation, Finished
    }

    /**
     * Checks if the connections in the given map of task templates are acyclic.
     *
     * Therefore this routine implements depth first search.
     *
     * @return true if the connections are acyclic
     */
    fun isAcyclic(taskTemplates: Map<Int, TaskTemplate>): Boolean {
        val statusMap = HashMap<Int, ProcessingStatus>()
        for ((id, taskTemplate) in taskTemplates)
            statusMap.put(id,
                ProcessTemplateCycleDetection.ProcessingStatus.NotVisited
            )

        for ((id, taskTemplate) in taskTemplates)
            if (statusMap.get(id) == ProcessTemplateCycleDetection.ProcessingStatus.NotVisited) {
                val cycleDetected =
                    visitNode(
                        taskTemplate,
                        statusMap
                    )
                if (cycleDetected)
                    return false
            }

        return true
    }

    /**
     * Subroutine for depth first search that goes into depth recursively.
     *
     * @return true if there was a cycle detected
     */
    private fun visitNode(taskTemplate: TaskTemplate, statusMap: HashMap<Int, ProcessingStatus>): Boolean {
        statusMap.put(taskTemplate.id,
            ProcessTemplateCycleDetection.ProcessingStatus.InVisitation
        )
        for (successor in taskTemplate.successors) {
            if (statusMap.get(successor.id) == ProcessTemplateCycleDetection.ProcessingStatus.NotVisited) {
                val cycleDetected =
                    visitNode(
                        successor,
                        statusMap
                    )
                if (cycleDetected)
                    return true
            } else if (statusMap.get(successor.id) == ProcessTemplateCycleDetection.ProcessingStatus.InVisitation) {
                return true
            }
        }
        statusMap.put(taskTemplate.id,
            ProcessTemplateCycleDetection.ProcessingStatus.Finished
        )
        return false
    }
}
