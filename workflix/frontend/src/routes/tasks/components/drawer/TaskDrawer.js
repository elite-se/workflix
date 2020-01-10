// @flow

import React from 'react'
import { Drawer } from '@blueprintjs/core'
import TaskDrawerContent from './TaskDrawerContent'
import type { TaskTemplateType, TaskType } from '../../../../modules/datatypes/Task'
import type { UserRoleType, UserType } from '../../../../modules/datatypes/User'
import onOpenRemoveOverlayClass from '../../../../modules/common/onOpenRemoveOverlayClass'

type PropsType = {|
  selectedTask: ?TaskType,
  onClose: () => void,
  onTaskModified: (TaskType) => void,
  taskTemplates: Map<number, TaskTemplateType>,
  users: Map<string, UserType>,
  userRoles: Map<number, UserRoleType>
|}

class TaskDrawer extends React.Component<PropsType> {
  render () {
    const { selectedTask, taskTemplates, userRoles } = this.props
    const taskTemplate = selectedTask ? taskTemplates.get(selectedTask.taskTemplateId) : undefined
    return <Drawer
      size={Drawer.SIZE_SMALL}
      hasBackdrop={false}
      isOpen={selectedTask != null}
      title={taskTemplate?.name || ''}
      onClose={this.props.onClose}
      onOpening={onOpenRemoveOverlayClass}
      style={{ overflow: 'auto' }}>
      {selectedTask && <TaskDrawerContent
          task={selectedTask}
          userRoles={userRoles}
          onTaskModified={this.props.onTaskModified}
          users={this.props.users}
          taskTemplates={this.props.taskTemplates}/>}
    </Drawer>
  }
}

export default TaskDrawer
