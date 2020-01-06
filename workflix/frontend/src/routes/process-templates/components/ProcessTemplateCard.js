// @flow

import React from 'react'
import { Button, Card, Colors, H3, Icon } from '@blueprintjs/core'
import type { ProcessTemplateType } from '../../../modules/datatypes/Process'
import { Link } from '@reach/router'
import type { StyledComponent } from 'styled-components'
import styled from 'styled-components'
import type { UserType } from '../../../modules/datatypes/User'
import { calcGraph } from '../../../modules/process-template-editor/graph-utils'
import ProcessChart from '../../../modules/process-template-editor/components/ProcessChart'
import type { TaskTemplateType } from '../../../modules/datatypes/Task'

const CustomLink: StyledComponent<{}, {}, *> = styled(Link)`
  margin: 5px;
  color: black;
  &:hover {
    text-decoration: none;
    color: black;
  }
  > * {
    padding-bottom: 0;
  }
`
const Item: StyledComponent<{}, {}, *> = styled('div')`
  display: flex;
  flex-direction: row;
  align-items: center;
  & > * {
    padding: 5px;
  }
`
const AllTheWay: StyledComponent<{}, {}, *> = styled('div')`
  display: flex;
  flex-direction: row;
  align-items: center;
  margin: 0 -20px;
  margin-top: 10px;
  padding: 10px;
  background-color: ${Colors.LIGHT_GRAY4};
`

type PropsType = { template: ProcessTemplateType, users: Map<string, UserType> }

class ProcessTemplateCard extends React.Component<PropsType> {
  render () {
    const { users, template } = this.props
    const owner = users.get(template.ownerId)
    const graph = calcGraph<number, TaskTemplateType>(template.taskTemplates)
    const criticalLength = Math.max(...graph.map(task => task.endDate))
    return <CustomLink to={`./edit/${template.id}`}>
        <Card style={{
          width: '350px',
          margin: '10px'
        }} interactive>
          <H3>{template.title}</H3>
          <Item><Icon icon='stopwatch'/><span>Duration Limit of {template.durationLimit}</span></Item>
          <Item><Icon icon='stopwatch'/><span>Critical Path of length {criticalLength}</span></Item>
          <Item><Icon
            icon='refresh'/><span>{template.runningProcesses} of {template.processCount} processes running</span></Item>
          <Item><Icon icon='time'/><span>Created at {template.createdAt.toLocaleString()}</span></Item>
          {owner && <Item><Icon icon='user'/><span>{`${owner.name} (${owner.displayname})`}</span></Item>}
          <Item><Button icon='play' fill style={{ margin: '10px 0' }} intent='success'>Start Process...</Button></Item>
          <AllTheWay><ProcessChart mini tasks={graph}/></AllTheWay>
        </Card>
      </CustomLink>
  }
}

export default ProcessTemplateCard
