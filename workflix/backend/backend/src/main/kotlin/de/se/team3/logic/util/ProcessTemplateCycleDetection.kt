package de.se.team3.logic.domain.processTemplateUtil

import de.se.team3.logic.domain.TaskTemplate

object ProcessTemplateCycleDetection {

    /**
     * Indicates whether a node was not visited yet, was already visited in the same
     * run of visitNode() or the visitation is finished.
     */
    enum class ProcessingStatus {
        NotVisited, InVisitation, Finished
    }

    /**
     * Checks if the connections in the given map of task templates are acyclic.
     *
     * This routine implements depth first search.
     *
     * @return true if the connections are acyclic
     */
    fun isAcyclic(taskTemplates: Map<Int, TaskTemplate>): Boolean {
        // setup: at the beginning each node is NotVisited
        val statusMap = HashMap<Int, ProcessingStatus>()
        for ((id, taskTemplate) in taskTemplates)
            statusMap.put(id,
                ProcessTemplateCycleDetection.ProcessingStatus.NotVisited
            )
        // As long as there are nodes that are not visited by visitNode() a new recursive visitation is started.
        for ((id, taskTemplate) in taskTemplates)
            if (statusMap.get(id) == ProcessTemplateCycleDetection.ProcessingStatus.NotVisited) {
                val cycleDetected =
                    visitNode(
                        taskTemplate,
                        statusMap
                    )
                // Terminates prematurely when a cycle is detected
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
        for (successor in taskTemplate.getSuccessors()) {
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
