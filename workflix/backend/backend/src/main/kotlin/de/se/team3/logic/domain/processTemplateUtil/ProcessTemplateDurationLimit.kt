package de.se.team3.logic.domain.processTemplateUtil

import de.se.team3.logic.domain.TaskTemplate
import kotlin.math.min

object ProcessTemplateDurationLimit {

    fun getDurationLimit(taskTemplates: Map<Int, TaskTemplate>): Int? {
        val highestDurationLimitFound = HashMap<Int, Int>()
        for ((templateId, taskTemplate) in taskTemplates) {
            if (taskTemplate.durationLimit == null)
                return null

            highestDurationLimitFound.put(templateId, taskTemplate.durationLimit)
        }

        for ((templateId, taskTemplate) in taskTemplates)
            if (taskTemplate.predecessors.size == 0)
                visitNode(taskTemplate, highestDurationLimitFound)

        return 0
    }

    private fun zeroIfNull(param: Int?): Int {
        if (param == null)
            return 0

        return param
    }

    fun visitNode(taskTemplate: TaskTemplate, highestDurationLimitFound: HashMap<Int, Int>) {
        taskTemplate.successors.forEach { successorTemplate ->
            val limitOnCurrentPath = taskTemplate.durationLimit!! + successorTemplate.durationLimit!!
            val formerLimit = highestDurationLimitFound.get(successorTemplate.id)

            val durationLimitOnCurrentPath = zeroIfNull(taskTemplate.durationLimit) + zeroIfNull(successorTemplate.durationLimit)
            val formerDurationLimit = highestDurationLimitFound.get(successorTemplate.id)!!

            highestDurationLimitFound.put(successorTemplate.id, min(durationLimitOnCurrentPath, formerDurationLimit))
        }
    }
}
