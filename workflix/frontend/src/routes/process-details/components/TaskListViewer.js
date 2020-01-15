// @flow

import { Colors, Drawer, FormGroup } from '@blueprintjs/core'
import React from 'react'
import type { TaskTemplateType, TaskType } from '../../../modules/datatypes/Task'
import TaskList from '../../../modules/process-template-editor/components/TaskList'
import ProcessChart, { chartNodeFromProcessedNode } from '../../../modules/process-template-editor/components/ProcessChart'
import onOpenRemoveOverlayClass from '../../../modules/common/onOpenRemoveOverlayClass'
import type { UserRoleType, UserType } from '../../../modules/datatypes/User'
import TaskDrawerContent from '../../tasks/components/drawer/TaskDrawerContent'
import calcTasksGraph from '../../../modules/process-template-editor/calcTasksGraph'

type PropsType = {
  users: Map<string, UserType>,
  userRoles: Map<number, UserRoleType>,
  taskTemplates: Map<number, TaskTemplateType>,
  tasks: TaskType[],
  setDrawerOpened: (drawerOpened: boolean) => void,
  onTaskModified: () => void,
  isReadOnly: boolean
}

type StateType = {
  selectedTaskId: ?number
}

const colorTranslation = {
  BLOCKED: {
    selected: Colors.GRAY4,
    unselected: Colors.GRAY1
  },
  RUNNING: {
    selected: Colors.ORANGE4,
    unselected: Colors.ORANGE1
  },
  CLOSED: {
    selected: Colors.GREEN4,
    unselected: Colors.GREEN1
  }
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
    const { taskTemplates, tasks, users, onTaskModified, userRoles, isReadOnly } = this.props
    const { selectedTaskId } = this.state
    const processedNodes = calcTasksGraph(tasks, taskTemplates)
    const chartNodes = processedNodes.map(node => chartNodeFromProcessedNode(
      node,
      colorTranslation[node.data.task.status][node.id === selectedTaskId ? 'selected' : 'unselected']
    ))
    const task = tasks.find(task => task.id === selectedTaskId)
    const template = task && taskTemplates.get(task.taskTemplateId)
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
      <Drawer size={Drawer.SIZE_SMALL} hasBackdrop={false} isOpen={task != null} title={template?.name || ''}
              onClose={this.unselectTask} style={{ overflow: 'auto' }} onOpening={onOpenRemoveOverlayClass}>
        {task && <TaskDrawerContent task={task} taskTemplates={taskTemplates} onTaskModified={onTaskModified}
                                    users={users} userRoles={userRoles} isReadOnly={isReadOnly}/>}
      </Drawer>
    </FormGroup>
  }
}

export default TaskListViewer
