// @flow

import React from 'react'
import { Drawer } from '@blueprintjs/core'
import type { TaskTemplateType, TaskType } from '../../../modules/datatypes/Task'
import TaskDrawerContent from './TaskDrawerContent'
import type { UserType } from '../../../modules/datatypes/User'

type PropsType = {|
  selectedTask: ?TaskType,
  onClose: () => void,
  onTaskModified: (TaskType) => void,
  taskTemplates: Map<number, TaskTemplateType>,
  users: Map<string, UserType>
|}

class TaskDrawer extends React.Component<PropsType> {
  // pretty hacky: "remove" the class of the Blueprint.js overlay between portal and drawer that prevents clicks in
  // the rest of the page
  onDrawerOpening = (elem: HTMLElement) => {
    const overlayElem = elem.parentElement
    if (overlayElem == null) {
      return
    }
    overlayElem.classList.remove('bp3-overlay-container')
  }

  render () {
    const selectedTask = this.props.selectedTask
    const taskTemplate = selectedTask ? this.props.taskTemplates.get(selectedTask.taskTemplateId) : undefined
    return <Drawer
      size={Drawer.SIZE_SMALL}
      hasBackdrop={false}
      isOpen={selectedTask != null}
      title={taskTemplate ? taskTemplate.name : ''}
      onClose={this.props.onClose}
      onOpening={this.onDrawerOpening}
      style={{ overflow: 'auto' }}
      users={this.props.users}>
      {selectedTask != null
        ? <TaskDrawerContent
          task={selectedTask}
          onTaskModified={this.props.onTaskModified}
          users={this.props.users}
          taskTemplates={this.props.taskTemplates}/>
        : ''}
    </Drawer>
  }
}

export default TaskDrawer
