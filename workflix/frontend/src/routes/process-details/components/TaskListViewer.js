// @flow

import { Colors, Drawer, FormGroup } from '@blueprintjs/core'
import React from 'react'
import type { TaskTemplateType, TaskType } from '../../../modules/datatypes/Task'
import { calcGraph } from '../../../modules/process-template-editor/graph-utils'
import TaskList from '../../../modules/process-template-editor/components/TaskList'
import ProcessChart, { chartNodeFromProcessedNode } from '../../../modules/process-template-editor/components/ProcessChart'
import onOpenRemoveOverlayClass from '../../../modules/common/onOpenRemoveOverlayClass'
import type { UserRoleType } from '../../../modules/datatypes/User'

type PropsType = {
  userRoles: Map<number, UserRoleType>,
  taskTemplates: TaskTemplateType[],
  tasks: TaskType[],
  setDrawerOpened: (drawerOpened: boolean) => void
}

type StateType = {
  selectedTaskId: ?number
}

class TaskListViewer extends React.Component<PropsType, StateType> {
  state = {
    selectedTaskId: null
  }

  selectTaskId = (id: number) => {
    this.setState({ selectedTaskId: id })
    this.props.setDrawerOpened(true)
  }

  unselectTask = () => {
    this.setState({ selectedTaskId: null })
    this.props.setDrawerOpened(false)
  }

  render () {
    const { taskTemplates, tasks } = this.props
    const { selectedTaskId } = this.state

    const task = taskTemplates.find(task => task.id === selectedTaskId)
    const preparedTasks = tasks.map(task => {
      const template = taskTemplates.find(template => template.id === task.taskTemplateId)
      if (!template) {
        return null
      }
      const predecessors = template.predecessors
        .map(pred => tasks.find(pTask => pTask.taskTemplateId === pred))
        .filter(Boolean).map(pred => pred.id)
      return {
        ...task,
        name: template.name,
        estimatedDuration: template.estimatedDuration,
        predecessors
      }
    }).filter(Boolean)
    const processedNodes = calcGraph(preparedTasks)
    const colorTranslation = {
      BLOCKED: {
        selected: Colors.GRAY4,
        unselected: Colors.GRAY1
      },
      RUNNING: {
        selected: Colors.GREEN4,
        unselected: Colors.GREEN1
      },
      CLOSED: {
        selected: Colors.ORANGE4,
        unselected: Colors.ORANGE1
      }
    }
    const chartNodes = processedNodes.map(node => chartNodeFromProcessedNode(
      node,
      colorTranslation[node.data.status][node.id === selectedTaskId ? 'selected' : 'unselected']
    ))
    return <FormGroup label='Tasks'>
      <div style={{
        display: 'flex',
        borderRadius: '3px',
        padding: '10px',
        border: `1px solid ${Colors.LIGHT_GRAY1}`
      }}>
        <TaskList selectedId={selectedTaskId} taskTemplates={processedNodes.map(node => node.data)}
                  selectTaskId={this.selectTaskId}/>
        <ProcessChart tasks={chartNodes} selectedId={selectedTaskId} selectTaskId={this.selectTaskId}/>
      </div>
      <Drawer size={Drawer.SIZE_SMALL} hasBackdrop={false} isOpen={task != null} title={task?.name || ''}
              onClose={this.unselectTask} style={{ overflow: 'auto' }} onOpening={onOpenRemoveOverlayClass}/>
    </FormGroup>
  }
}

export default TaskListViewer
