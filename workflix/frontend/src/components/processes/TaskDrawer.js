// @flow

import React from 'react'
import { Drawer } from '@blueprintjs/core'
import type { TaskType } from '../../datatypes/TaskType'
import TaskDrawerContent from './TaskDrawerContent'
import type { UserType } from '../../datatypes/models'

type PropsType = {|
  selectedTask: ?TaskType,
  onClose: () => void,
  onTaskModified: (TaskType) => void,
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
    return <Drawer
      size={Drawer.SIZE_SMALL}
      hasBackdrop={false}
      isOpen={selectedTask != null}
      title={selectedTask != null ? selectedTask.templateName : ''}
      onClose={this.props.onClose}
      onOpening={this.onDrawerOpening}
      style={{ overflow: 'auto' }}
      users={this.props.users}>
      {selectedTask != null
        ? <TaskDrawerContent task={selectedTask} onTaskModified={this.props.onTaskModified} users={this.props.users} />
        : ''}
    </Drawer>
  }
}

export default TaskDrawer
